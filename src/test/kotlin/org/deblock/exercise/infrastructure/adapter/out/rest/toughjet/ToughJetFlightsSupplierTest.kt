package org.deblock.exercise.infrastructure.adapter.out.rest.toughjet

import kotlinx.coroutines.runBlocking
import org.deblock.exercise.domain.model.*
import org.deblock.exercise.domain.model.ExternalSupplierName.TOUGH_JET
import org.deblock.exercise.test.BaseFunctionalTest
import org.deblock.exercise.test.TestFixtures.EUR
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class ToughJetFlightsSupplierTest : BaseFunctionalTest() {

    @Autowired
    lateinit var toughJetFlightsSupplier: ToughJetFlightsSupplier

    @Test
    fun `should fetch and map flights from ToughJet API`() {
        // given
        //language=JSON
        toughJetWiremockServer.stubResponse(
            requestBody = """
            {
                "from": "LHR",
                "to": "AMS",
                "outboundDate": "2025-08-16",
                "inboundDate": "2025-08-17",
                "numberOfAdults": 1
            }
            """,
            responseBody = """[
            {
                "carrier": "ToughAirline",
                "basePrice": {
                    "amount": 100.00,
                    "currency": "EUR"
                },
                "tax": {
                    "amount": 20.00,
                    "currency": "EUR"
                },
                "discount": 0,
                "departureAirportName": "LHR",
                "arrivalAirportName": "AMS",
                "outboundDateTime": "2025-08-16T11:00:00Z",
                "inboundDateTime": "2025-08-17T13:00:00Z"
            }
          ]"""
        )

        val expectedResponse = Flight(
            airline = AirlineName("ToughAirline"),
            supplier = TOUGH_JET,
            fare = money(EUR, BigDecimal("120.00")),
            originAirport = IataCode("LHR"),
            destinationAirport = IataCode("AMS"),
            departureDate = LocalDateTime.parse("2025-08-16T11:00:00"),
            arrivalDate = LocalDateTime.parse("2025-08-17T13:00:00")
        )

        // when
        val actualResponse = runBlocking { toughJetFlightsSupplier.search(request) }

        // then
        assertEquals(expectedResponse, actualResponse.single())
    }

    @Test
    fun `should propagate errors`() {
        // given
        toughJetWiremockServer.stubErrorResponse()

        // when
        assertThrows(
            Exception::class.java
        ) { runBlocking { toughJetFlightsSupplier.search(request) } }
    }

    companion object {
        private val request = FlightSearchRequest(
            origin = IataCode("LHR"),
            destination = IataCode("AMS"),
            departureDate = LocalDate.parse("2025-08-16"),
            returnDate = LocalDate.parse("2025-08-17"),
            numberOfPassengers = 1
        )
    }
}