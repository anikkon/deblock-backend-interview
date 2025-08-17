package org.deblock.exercise.domain.model

import org.deblock.exercise.test.TestFixtures.EUR
import org.deblock.exercise.test.TestFixtures.USD
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MoneyTest {

    @Test
    fun `should create Money with valid amount and round to 2 decimal places`() {
        // given
        val amount = BigDecimal("100.12345")
        val expected = BigDecimal("100.12") // Rounded with HALF_UP

        // when
        val money = money(EUR, amount)

        // then
        assertEquals(EUR, money.currency)
        assertEquals(expected, money.amount)
    }

    @Test
    fun `should throw IllegalArgumentException for negative amount`() {
        // given
        val amount = BigDecimal("-0.01")

        // when, then
        assertThrows(IllegalArgumentException::class.java) {
            money(EUR, amount)
        }
    }

    @Test
    fun `should add Money with same currency correctly`() {
        // given
        val money1 = money(EUR, BigDecimal("100.50"))
        val money2 = money(EUR, BigDecimal("50.25"))
        val expected = money(EUR, BigDecimal("150.75"))

        // when
        val result = money1.add(money2)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `should throw IllegalArgumentException when adding Money with different currencies`() {
        // given
        val money1 = money(EUR, BigDecimal("100.50"))
        val money2 = money(USD, BigDecimal("50.25"))

        // when, then
        assertThrows(IllegalArgumentException::class.java) {
            money1.add(money2)
        }
    }

    @Test
    fun `should multiply Money correctly`() {
        // given
        val money = money(EUR, BigDecimal("100.00"))
        val multiplier = BigDecimal("1.234")
        val expected = money(EUR, BigDecimal("123.40")) // 100 * 1.234 = 123.4

        // when
        val result = money.multiply(multiplier)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `should divide Money correctly`() {
        // given
        val money = money(EUR, BigDecimal("100.00"))
        val divisor = BigDecimal("3")
        val expected = money(EUR, BigDecimal("33.33")) // 100 / 3 = 33.333... rounded to 33.33

        // when
        val result = money.divide(divisor)

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `should throw ArithmeticException when dividing by zero`() {
        // given
        val money = money(EUR, BigDecimal("100.00"))
        val divisor = BigDecimal.ZERO

        // when, then
        assertThrows(ArithmeticException::class.java) {
            money.divide(divisor)
        }
    }

    @Test
    fun `should compare Money with same currency correctly`() {
        // given
        val money1 = money(EUR, BigDecimal("100.50"))
        val money2 = money(EUR, BigDecimal("50.25"))
        val money3 = money(EUR, BigDecimal("100.50"))

        // when, then
        assertEquals(1, money1.compareTo(money2))
        assertEquals(-1, money2.compareTo(money1))
        assertEquals(0, money1.compareTo(money3))
    }

    @Test
    fun `should throw IllegalArgumentException when comparing Money with different currencies`() {
        // given
        val money1 = money(EUR, BigDecimal("100.50"))
        val money2 = money(USD, BigDecimal("100.50"))

        // when, then
        assertThrows(IllegalArgumentException::class.java) {
            money1.compareTo(money2)
        }
    }
}