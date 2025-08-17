package org.deblock.exercise.test

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.deblock.exercise.ExerciseApplication
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(classes = [ExerciseApplication::class])
@ActiveProfiles("test")
class BaseFunctionalTest {

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    private lateinit var wireMockServer: WireMockServer
    protected lateinit var crazyAirWiremockServer: CrazyAirWiremockServer
    protected lateinit var toughJetWiremockServer: ToughJetWiremockServer
    protected lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        wireMockServer = WireMockServer(WireMockConfiguration().port(8080))
        wireMockServer.start()
        crazyAirWiremockServer = CrazyAirWiremockServer(wireMockServer)
        toughJetWiremockServer = ToughJetWiremockServer(wireMockServer)
    }

    @AfterEach
    fun teardown() {
        wireMockServer.stop()
    }
}