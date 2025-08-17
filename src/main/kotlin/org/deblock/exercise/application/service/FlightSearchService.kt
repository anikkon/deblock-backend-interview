package org.deblock.exercise.application.service

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.deblock.exercise.application.port.`in`.FlightsSearchUseCase
import org.deblock.exercise.application.port.out.FlightsSupplier
import org.deblock.exercise.domain.model.FlightSearchRequest
import org.deblock.exercise.domain.model.FlightSearchResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FlightSearchService(
    private val suppliers: Collection<FlightsSupplier>
) : FlightsSearchUseCase {

    override fun search(request: FlightSearchRequest) = runBlocking {
        suppliers
            .map { supplier ->
                async {
                    runCatching { supplier.search(request) }.getOrElse {
                        logger.error("Error in supplier ${supplier.name}", it)
                        // Ignore erroneous suppliers,
                        // aiming to provide a response despite partial unavailability
                        emptyList()
                    }
                }
            }.awaitAll()
            .let { FlightSearchResponse(it.flatten()) }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FlightSearchService::class.java)
    }
}