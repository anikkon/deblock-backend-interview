package org.deblock.exercise.infrastructure.adapter.out.rest.crazyair

import org.deblock.exercise.domain.model.AirlineName
import org.deblock.exercise.domain.model.ExternalSupplierName.CRAZY_AIR
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchRequest
import org.deblock.exercise.domain.model.IataCode
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

internal object DtoMapper {
    fun FlightSearchRequest.toDto() = CrazyAirRequest(
        origin = this.origin.name,
        destination = this.destination.name,
        departureDate = this.departureDate.format(ISO_LOCAL_DATE),
        returnDate = this.returnDate.format(ISO_LOCAL_DATE),
        passengerCount = this.numberOfPassengers
    )

    fun Array<CrazyAirResponse>.fromDto() = this.map { it.fromDto() }

    fun CrazyAirResponse.fromDto(): Flight =
        Flight(
            airline = AirlineName(this.airline),
            supplier = CRAZY_AIR,
            fare = this.price,
            originAirport = IataCode(this.departureAirportCode),
            destinationAirport = IataCode(this.destinationAirportCode),
            departureDate = this.departureDate,
            arrivalDate = this.arrivalDate
        )
}