package org.deblock.exercise.application.port.out

import org.deblock.exercise.domain.model.ExternalSupplierName
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchRequest

interface FlightsSupplier {
    val name: ExternalSupplierName
    suspend fun search(request: FlightSearchRequest): List<Flight>
}