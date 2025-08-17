package org.deblock.exercise.test

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class ToughJetWiremockServer(
    private var wireMockServer: WireMockServer
) {

    fun stubErrorResponse() {
        wireMockServer.stubFor(
            WireMock.post("/toughjet")
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
            WireMock.post("/toughjet")
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
            WireMock.post("/toughjet")
                .withRequestBody(equalToJson(requestBody, true, true))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(responseBody)
                )
        )
    }
}