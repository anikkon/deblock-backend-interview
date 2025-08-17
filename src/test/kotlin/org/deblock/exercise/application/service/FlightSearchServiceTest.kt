package org.deblock.exercise.application.service

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.deblock.exercise.application.port.out.FlightsSupplier
import org.deblock.exercise.test.TestFixtures.aFlight
import org.deblock.exercise.test.TestFixtures.aFlightSearchRequest
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*

class FlightSearchServiceTest {

    private val supplier1 = mock(FlightsSupplier::class.java)
    private val supplier2 = mock(FlightsSupplier::class.java)

    private val service = FlightSearchService(listOf(supplier1, supplier2))

    @Test
    fun `aggregates flights from multiple suppliers`(): Unit = runBlocking {
        // given
        given(supplier1.search(request)).willReturn(listOf(flight1))
        given(supplier2.search(request)).willReturn(listOf(flight2))

        // when
        val result = service.search(request)

        // then
        assertThat(result.flights).containsExactlyInAnyOrder(flight1, flight2)

        verify(supplier1, times(1)).search(request)
        verify(supplier2, times(1)).search(request)
    }

    @Test
    fun `ignores errors from suppliers and returns successful results`(): Unit = runBlocking {
        // given
        given(supplier1.search(request)).willReturn(listOf(flight1))
        given(supplier2.search(request)).willThrow(RuntimeException())

        // when
        val result = service.search(request)

        // then
        assertThat(result.flights).containsExactlyInAnyOrder(flight1)

        verify(supplier1, times(1)).search(request)
        verify(supplier2, times(1)).search(request)
    }

    companion object {
        private val request = aFlightSearchRequest()
        private val flight1 = aFlight()
        private val flight2 = aFlight()
    }
}
