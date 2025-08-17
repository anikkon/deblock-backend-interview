package org.deblock.exercise.domain.model

import java.time.LocalDateTime

data class Flight(
    val airline: AirlineName,
    val supplier: ExternalSupplierName,
    val originAirport: IataCode,
    val destinationAirport: IataCode,
    val departureDate: LocalDateTime,
    val arrivalDate: LocalDateTime,
    val fare: Money,
)
