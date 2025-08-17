package org.deblock.exercise.domain.model

import java.time.LocalDate

data class FlightSearchRequest(
    val origin: IataCode,
    val destination: IataCode,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val numberOfPassengers: Int,
)
