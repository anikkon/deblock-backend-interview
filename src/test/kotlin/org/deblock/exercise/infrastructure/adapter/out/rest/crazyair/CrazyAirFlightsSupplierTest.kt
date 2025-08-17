package org.deblock.exercise.infrastructure.adapter.out.rest.crazyair

import kotlinx.coroutines.runBlocking
import org.deblock.exercise.domain.model.*
import org.deblock.exercise.domain.model.ExternalSupplierName.CRAZY_AIR
import org.deblock.exercise.test.BaseFunctionalTest
import org.deblock.exercise.test.TestFixtures.EUR
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class CrazyAirFlightsSupplierTest : BaseFunctionalTest() {

    @Autowired
    lateinit var crazyAirFlightsSupplier: CrazyAirFlightsSupplier

    @Test
    fun `should fetch and map flights from CrazyAir API`() {
        // given
        //language=JSON
        crazyAirWiremockServer.stubResponse(
            requestBody = """
            {
                "origin": "LHR",
                "destination": "AMS",
                "departureDate": "2025-08-16",
                "returnDate": "2025-08-17",
                "passengerCount": 1
            }
            """,
            responseBody = """[
            {
                "airline": "CrazyAirline",
                "price": {
                    "amount": 100.00,
                    "currency": "EUR"
                },
                "cabinclass": "E",
                "departureAirportCode": "LHR",
                "destinationAirportCode": "AMS",
                "departureDate": "2025-08-16T10:00:00",
                "arrivalDate": "2025-08-17T12:00:00"
            }
           ]"""
        )

        val expectedResponse = Flight(
            airline = AirlineName("CrazyAirline"),
            supplier = CRAZY_AIR,
            fare = money(EUR, BigDecimal("100.00")),
            originAirport = IataCode("LHR"),
            destinationAirport = IataCode("AMS"),
            departureDate = LocalDateTime.parse("2025-08-16T10:00:00"),
            arrivalDate = LocalDateTime.parse("2025-08-17T12:00:00")
        )

        // when
        val actualResponse = runBlocking { crazyAirFlightsSupplier.search(request) }

        // then
        assertEquals(expectedResponse, actualResponse.single())
    }

    @Test
    fun `should propagate errors`() {
        // given
        crazyAirWiremockServer.stubErrorResponse()

        // when
        assertThrows(
            Exception::class.java
        ) { runBlocking { crazyAirFlightsSupplier.search(request) } }
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