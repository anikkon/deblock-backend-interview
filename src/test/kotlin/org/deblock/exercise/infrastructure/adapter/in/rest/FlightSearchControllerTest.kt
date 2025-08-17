package org.deblock.exercise.infrastructure.adapter.`in`.rest

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.deblock.exercise.test.BaseFunctionalTest
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.post

class FlightSearchControllerFunctionalTest : BaseFunctionalTest() {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return sorted flights from suppliers for valid search request`() {
        // given
        //language=JSON
        crazyAirWiremockServer.stubResponse(
            """[
                    {
                        "airline": "CrazyAirline",
                        "price": {
                            "amount": 100.00,
                            "currency": "EUR"
                        },
                        "cabinclass": "E",
                        "departureAirportCode": "LHR",
                        "destinationAirportCode": "AMS",
                        "departureDate": "2025-08-16T10:00:00",
                        "arrivalDate": "2025-08-17T12:00:00"
                    }
                ]"""
        )
        //language=JSON
        toughJetWiremockServer.stubResponse(
            """[
                {
                    "carrier": "ToughAirline",
                    "basePrice": {
                        "amount": 80.00,
                        "currency": "EUR"
                    },
                    "tax": {
                        "amount": 20.00,
                        "currency": "EUR"
                    },
                    "discount": 10,
                    "departureAirportName": "LHR",
                    "arrivalAirportName": "AMS",
                    "outboundDateTime": "2025-08-16T11:00:00Z",
                    "inboundDateTime": "2025-08-17T13:00:00Z"
                }
            ]"""
        )

        val requestDto = mapOf(
            "origin" to "LHR",
            "destination" to "AMS",
            "departureDate" to "2025-08-16",
            "returnDate" to "2025-08-17",
            "numberOfPassengers" to 1
        )

        @Language("JSON") val expectedResponse = """[
            {
                "airline": "ToughAirline",
                "supplier": "TOUGH_JET",
                "origin": "LHR",
                "destination": "AMS",
                "arrivalDate": "2025-08-17T13:00:00",
                "departureDate": "2025-08-16T11:00:00",
                "fare": {
                    "currency": "EUR",
                    "amount": 92.00
                }
            },
            {
                "airline": "CrazyAirline",
                "supplier": "CRAZY_AIR",
                "origin": "LHR",
                "destination": "AMS",
                "arrivalDate": "2025-08-17T12:00:00",
                "departureDate": "2025-08-16T10:00:00",
                "fare": {
                    "currency": "EUR",
                    "amount": 100.00
                }
            }
        ]"""

        // when
        val result = mockMvc.post("/flights/search") {
            contentType = APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestDto)
        }.andExpect {
            status { isOk() }
            content { contentType(APPLICATION_JSON) }
        }.andReturn()

        // then
        val actualResponse = result.response.contentAsString
        assertThat(actualResponse).isEqualToIgnoringWhitespace(expectedResponse)
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    fun `should return 400 for invalid requests`(request: Any) {
        // when, then
        mockMvc.post("/flights/search") {
            contentType = APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @ParameterizedTest
    @MethodSource("validRequests")
    fun `should return 200 for valid requests`(request: Any) {
        // given
        crazyAirWiremockServer.stubResponse("[]")
        toughJetWiremockServer.stubResponse("[]")

        // when, then
        mockMvc.post("/flights/search") {
            contentType = APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
        }
    }

    companion object {
        @JvmStatic
        fun validRequests(): List<Arguments> = listOf(
            Arguments.of(
                mapOf(
                    "origin" to "LHA",
                    "destination" to "AMS",
                    "departureDate" to "2025-08-16",
                    "returnDate" to "2025-08-17",
                    "numberOfPassengers" to 1
                )
            ),
            Arguments.of(
                mapOf(
                    "origin" to "LHA",
                    "destination" to "AMS",
                    "departureDate" to "2025-08-16",
                    "returnDate" to "2025-08-17",
                    "numberOfPassengers" to 3
                )
            ),
            Arguments.of(
                mapOf(
                    "origin" to "LHA",
                    "destination" to "AMS",
                    "departureDate" to "2025-08-17",
                    "returnDate" to "2025-08-17",
                    "numberOfPassengers" to 1
                )
            ),
            Arguments.of(
                mapOf(
                    "origin" to "ABC",
                    "destination" to "ALC",
                    "departureDate" to "2025-08-16",
                    "returnDate" to "2030-08-17",
                    "numberOfPassengers" to 4
                )
            )
        )

        @JvmStatic
        fun invalidRequests(): List<Arguments> = listOf(
            Arguments.of(
                mapOf(
                    "origin" to "LH", // Invalid: only 2 letters
                    "destination" to "AMS",
                    "departureDate" to "2025-08-16",
                    "returnDate" to "2025-08-17",
                    "numberOfPassengers" to 1
                )
            ),
            Arguments.of(
                mapOf(
                    "origin" to "LHA",
                    "destination" to "LHA", // Invalid: Same as origin
                    "departureDate" to "2025-08-16",
                    "returnDate" to "2025-08-17",
                    "numberOfPassengers" to 1
                )
            ),
            Arguments.of(
                (mapOf(
                    "origin" to "LHA",
                    "destination" to "AMS",
                    "departureDate" to "2025-08-18", // Invalid: after return date
                    "returnDate" to "2025-08-17",
                    "numberOfPassengers" to 1
                )),
                Arguments.of(
                    mapOf(
                        "origin" to "LHA",
                        "destination" to "AMS",
                        "departureDate" to "2025-08-16",
                        "returnDate" to "2025-08-17",
                        "numberOfPassengers" to 0 // Invalid: negative
                    )
                ),
                Arguments.of(
                    mapOf(
                        "origin" to "LHA",
                        "destination" to "AMS",
                        "departureDate" to "2025-08-16",
                        "returnDate" to "2025-08-17",
                        "numberOfPassengers" to 5 // Invalid: > 4
                    )
                )
            ),
        )
    }
}