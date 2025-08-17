package org.deblock.exercise.test

import org.deblock.exercise.domain.model.*
import org.deblock.exercise.domain.model.ExternalSupplierName.CRAZY_AIR
import org.deblock.exercise.infrastructure.adapter.out.rest.toughjet.ToughJetResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.UUID.randomUUID

object TestFixtures {

    val EUR: Currency = Currency.getInstance("EUR")
    val USD: Currency = Currency.getInstance("USD")

    fun aFlightSearchRequest() =
        FlightSearchRequest(
            origin = IataCode("LHR"),
            destination = IataCode("AMS"),
            departureDate = LocalDate.now(),
            returnDate = LocalDate.now(),
            numberOfPassengers = 2
        )

    fun aFlight() =
        Flight(
            airline = AirlineName(randomUUID().toString()),
            supplier = CRAZY_AIR,
            originAirport = IataCode("LHR"),
            destinationAirport = IataCode("AMS"),
            departureDate = LocalDateTime.now(),
            arrivalDate = LocalDateTime.now().plusHours(2),
            fare = money(EUR, BigDecimal.valueOf(20L)),
        )

    fun aToughJetResponse(
        basePrice: Money = Money(EUR, BigDecimal("100.00")),
        discount: Long = 10L,
        tax: Money = Money(EUR, BigDecimal("20.00"))
    ) = ToughJetResponse(
        carrier = "ToughAirline",
        basePrice = basePrice,
        tax = tax,
        discount = discount,
        departureAirportName = "LHR",
        arrivalAirportName = "AMS",
        outboundDateTime = Instant.parse("2025-08-16T11:00:00Z"),
        inboundDateTime = Instant.parse("2025-08-17T13:00:00Z")
    )
}