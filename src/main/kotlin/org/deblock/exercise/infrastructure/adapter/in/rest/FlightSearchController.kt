package org.deblock.exercise.infrastructure.adapter.`in`.rest

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.deblock.exercise.application.port.`in`.FlightsSearchUseCase
import org.deblock.exercise.domain.model.AirlineName
import org.deblock.exercise.domain.model.ExternalSupplierName
import org.deblock.exercise.domain.model.IataCode
import org.deblock.exercise.domain.model.Money
import org.deblock.exercise.infrastructure.adapter.`in`.rest.DtoMapper.fromDto
import org.deblock.exercise.infrastructure.adapter.`in`.rest.DtoMapper.toDto
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/flights")
class FlightSearchController(
    private val flightsSearchUseCase: FlightsSearchUseCase
) {
    @PostMapping("search")
    fun searchFlights(@Valid @RequestBody request: FlightSearchRequestDto): ResponseEntity<List<FlightDto>> {
        require(!request.returnDate.isBefore(request.departureDate)) {
            "Return data must not be before departure date"
        }
        require(request.origin != request.destination) {
            "Origin and destination must differ"
        }
        val response = flightsSearchUseCase
            .search(request.fromDto())
            .cheapestFirst()
            .toDto()
        return ok()
            .contentType(APPLICATION_JSON)
            .body(response)
    }
}

data class FlightSearchRequestDto(
    @field:NotNull val origin: String,
    @field:NotNull val destination: String,
    @field:NotNull val departureDate: LocalDate,
    @field:NotNull val returnDate: LocalDate,
    @field:Max(4) @field:Min(1) val numberOfPassengers: Int
)

data class FlightDto(
    val airline: AirlineName,
    val supplier: ExternalSupplierName,
    val origin: IataCode,
    val destination: IataCode,
    val arrivalDate: String,
    val departureDate: String,
    val fare: Money,
)
