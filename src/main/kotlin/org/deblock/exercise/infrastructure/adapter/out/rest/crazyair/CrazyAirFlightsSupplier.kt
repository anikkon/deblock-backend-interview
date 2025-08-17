package org.deblock.exercise.infrastructure.adapter.out.rest.crazyair

import kotlinx.coroutines.reactive.awaitSingle
import org.deblock.exercise.application.port.out.FlightsSupplier
import org.deblock.exercise.domain.model.ExternalSupplierName.CRAZY_AIR
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchRequest
import org.deblock.exercise.domain.model.Money
import org.deblock.exercise.infrastructure.adapter.out.rest.crazyair.DtoMapper.fromDto
import org.deblock.exercise.infrastructure.adapter.out.rest.crazyair.DtoMapper.toDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime

@Component
class CrazyAirFlightsSupplier(
    @Value("\${suppliers.crazyair.url}") private val url: String,
    private val webClient: WebClient
) : FlightsSupplier {

    override val name = CRAZY_AIR

    override suspend fun search(request: FlightSearchRequest): List<Flight> {
        logger.debug("Fetching flights from CrazyAir for request: {}", request)

        val result = webClient.post()
            .uri(url)
            .bodyValue(request.toDto())
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Array<CrazyAirResponse>>() {})
            .awaitSingle()
            .fromDto()

        logger.debug("Retrieved ${result.size} flights from CrazyAir")

        return result
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CrazyAirFlightsSupplier::class.java)
    }
}

data class CrazyAirRequest(
    val origin: String,
    val destination: String,
    val departureDate: String,
    val returnDate: String,
    val passengerCount: Int
)

data class CrazyAirResponse(
    val airline: String,
    val price: Money,
    val cabinclass: String,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: LocalDateTime,
    val arrivalDate: LocalDateTime
)