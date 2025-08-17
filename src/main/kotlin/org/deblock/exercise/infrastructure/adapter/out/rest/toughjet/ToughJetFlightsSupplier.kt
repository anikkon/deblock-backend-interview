package org.deblock.exercise.infrastructure.adapter.out.rest.toughjet

import kotlinx.coroutines.reactive.awaitSingle
import org.deblock.exercise.application.port.out.FlightsSupplier
import org.deblock.exercise.domain.model.ExternalSupplierName.TOUGH_JET
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchRequest
import org.deblock.exercise.domain.model.Money
import org.deblock.exercise.infrastructure.adapter.out.rest.toughjet.DtoMapper.fromDto
import org.deblock.exercise.infrastructure.adapter.out.rest.toughjet.DtoMapper.toDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.Instant

@Component
class ToughJetFlightsSupplier(
    @Value("\${suppliers.toughjet.url}") private val url: String,
    private val webClient: WebClient
) : FlightsSupplier {

    override val name = TOUGH_JET

    override suspend fun search(request: FlightSearchRequest): List<Flight> {
        logger.debug("Fetching flights from ToughJet for request: {}", request)

        val result = webClient.post()
            .uri(url)
            .bodyValue(request.toDto())
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Array<ToughJetResponse>>() {})
            .awaitSingle()
            .fromDto()

        logger.debug("Retrieved ${result.size} flights from ToughJet")

        return result
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ToughJetFlightsSupplier::class.java)
    }
}

data class ToughJetRequest(
    val from: String,
    val to: String,
    val outboundDate: String,
    val inboundDate: String,
    val numberOfAdults: Int
)

data class ToughJetResponse(
    val carrier: String,
    val basePrice: Money, // Price without tax(doesn't include discount)
    val tax: Money,
    val discount: Long, // Discount which needs to be applied on the price(in percentage) - 0..100
    val departureAirportName: String,
    val arrivalAirportName: String,
    val outboundDateTime: Instant,
    val inboundDateTime: Instant
)