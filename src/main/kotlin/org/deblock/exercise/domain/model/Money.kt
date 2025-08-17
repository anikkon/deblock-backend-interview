package org.deblock.exercise.domain.model

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.HALF_UP
import java.util.*

data class Money internal constructor(val currency: Currency, val amount: BigDecimal) : Comparable<Money> {

    init {
        require(amount >= ZERO) { "Money amount must be non-negative, but was: $amount" }
    }

    fun add(other: Money): Money {
        checkSameCurrency(other)
        return money(this.currency, this.amount + other.amount)
    }

    fun multiply(other: BigDecimal) =
        money(this.currency, this.amount.multiply(other))

    fun divide(other: BigDecimal) =
        money(this.currency, this.amount.divide(other, 2, HALF_UP))

    override fun compareTo(other: Money): Int {
        checkSameCurrency(other)
        return this.amount.compareTo(other.amount)
    }

    private fun checkSameCurrency(other: Money) =
        require(this.currency == other.currency) {
            "Currencies don't match"
        }
}

fun money(currency: Currency, amount: BigDecimal) =
    Money(
        currency,
        amount.setScale(2, HALF_UP)
    )