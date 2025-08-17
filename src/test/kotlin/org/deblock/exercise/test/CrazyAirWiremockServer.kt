package org.deblock.exercise.test

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class CrazyAirWiremockServer(
    private var wireMockServer: WireMockServer
) {

    fun stubErrorResponse() {
        wireMockServer.stubFor(
            WireMock.post("/crazyair")
                .willReturn(
                    aResponse()
                        .withStatus(500)
                )
        )
    }

    fun stubResponse(
        responseBody: String
    ) {
        wireMockServer.stubFor(
            WireMock.post("/crazyair")
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(responseBody)
                )
        )
    }

    fun stubResponse(
        requestBody: String,
        responseBody: String
    ) {
        wireMockServer.stubFor(
            WireMock.post("/crazyair")
                .withRequestBody(WireMock.equalToJson(requestBody, true, true))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(responseBody)
                )
        )
    }
}