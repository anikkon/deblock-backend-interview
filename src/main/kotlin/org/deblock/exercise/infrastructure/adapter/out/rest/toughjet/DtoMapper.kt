package org.deblock.exercise.infrastructure.adapter.out.rest.toughjet

import org.deblock.exercise.domain.model.AirlineName
import org.deblock.exercise.domain.model.ExternalSupplierName.TOUGH_JET
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchRequest
import org.deblock.exercise.domain.model.IataCode
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

internal object DtoMapper {
    fun FlightSearchRequest.toDto() = ToughJetRequest(
        from = this.origin.name,
        to = this.destination.name,
        outboundDate = this.departureDate.format(ISO_LOCAL_DATE),
        inboundDate = this.returnDate.format(ISO_LOCAL_DATE),
        numberOfAdults = this.numberOfPassengers
    )

    fun Array<ToughJetResponse>.fromDto() = this.map { it.fromDto() }

    fun ToughJetResponse.fromDto(): Flight =
        Flight(
            airline = AirlineName(this.carrier),
            supplier = TOUGH_JET,
            fare = ToughJetResponseFareCalculator.calculateTotalFare(this),
            originAirport = IataCode(this.departureAirportName),
            destinationAirport = IataCode(this.arrivalAirportName),
            // Note: assuming UTC conversion for the lack of other information
            departureDate = LocalDateTime.ofInstant(this.outboundDateTime, UTC),
            arrivalDate = LocalDateTime.ofInstant(this.inboundDateTime, UTC)
        )
}