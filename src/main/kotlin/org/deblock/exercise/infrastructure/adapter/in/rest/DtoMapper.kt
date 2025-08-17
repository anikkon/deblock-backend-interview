package org.deblock.exercise.infrastructure.adapter.`in`.rest

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchRequest
import org.deblock.exercise.domain.model.IataCode
import java.time.format.DateTimeFormatter.ISO_DATE_TIME

internal object DtoMapper {

    fun Collection<Flight>.toDto() = this.map { it.toDto() }

    fun Flight.toDto() = FlightDto(
        airline = this.airline,
        supplier = this.supplier,
        origin = this.originAirport,
        destination = this.destinationAirport,
        arrivalDate = this.arrivalDate.format(ISO_DATE_TIME),
        departureDate = this.departureDate.format(ISO_DATE_TIME),
        fare = this.fare
    )

    fun FlightSearchRequestDto.fromDto() = FlightSearchRequest(
        origin = IataCode(this.origin),
        destination = IataCode(this.destination),
        departureDate = this.departureDate,
        returnDate = this.returnDate,
        numberOfPassengers = this.numberOfPassengers
    )
}