package org.deblock.exercise.application.port.`in`

import org.deblock.exercise.domain.model.FlightSearchRequest
import org.deblock.exercise.domain.model.FlightSearchResponse

@FunctionalInterface
fun interface FlightsSearchUseCase {
    fun search(request: FlightSearchRequest): FlightSearchResponse
}