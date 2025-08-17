package org.deblock.exercise.infrastructure.adapter.out.rest.toughjet

import org.deblock.exercise.domain.model.Money
import org.deblock.exercise.infrastructure.adapter.out.rest.toughjet.ToughJetResponseFareCalculator.calculateTotalFare
import org.deblock.exercise.test.TestFixtures.EUR
import org.deblock.exercise.test.TestFixtures.USD
import org.deblock.exercise.test.TestFixtures.aToughJetResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ToughJetResponseFareCalculatorTest {

    @Test
    fun `should calculate total fare with discount correctly`() {
        // given
        val response = aToughJetResponse(
            basePrice = Money(EUR, BigDecimal("100.00")),
            tax = Money(EUR, BigDecimal("20.00")),
            discount = 10L,
        )
        val expected = Money(EUR, BigDecimal("110.00"))

        // when
        val result = calculateTotalFare(response)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `should calculate total fare with zero discount correctly`() {
        // given
        val response = aToughJetResponse(
            basePrice = Money(EUR, BigDecimal("100.00")),
            tax = Money(EUR, BigDecimal("20.00")),
            discount = 0L
        )
        val expected = Money(EUR, BigDecimal("120.00"))

        // when
        val result = calculateTotalFare(response)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `should calculate total fare with maximum discount correctly`() {
        // given
        val response = aToughJetResponse(
            basePrice = Money(EUR, BigDecimal("100.00")),
            tax = Money(EUR, BigDecimal("20.00")),
            discount = 100L
        )
        val expected = Money(EUR, BigDecimal("20.00"))

        // when
        val result = calculateTotalFare(response)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `should throw IllegalArgumentException when basePrice and tax have different currencies`() {
        // given
        val response = aToughJetResponse(
            basePrice = Money(EUR, BigDecimal("100.00")),
            tax = Money(USD, BigDecimal("20.00")),
            discount = 10L
        )

        // when, then
        assertThrows(IllegalArgumentException::class.java) {
            calculateTotalFare(response)
        }
    }

    @Test
    fun `should round the result`() {
        // given
        val response = aToughJetResponse(
            basePrice = Money(EUR, BigDecimal("100.12")),
            tax = Money(EUR, BigDecimal("20.67")),
            discount = 10L,
        )
        val expected = Money(EUR, BigDecimal("110.78"))

        // when
        val result = calculateTotalFare(response)

        // then
        assertEquals(expected, result)
    }
}