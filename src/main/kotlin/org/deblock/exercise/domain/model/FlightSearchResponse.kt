package org.deblock.exercise.domain.model

data class FlightSearchResponse(
    val flights: List<Flight>
) {
    fun cheapestFirst() = flights.sortedBy { it.fare }
}
