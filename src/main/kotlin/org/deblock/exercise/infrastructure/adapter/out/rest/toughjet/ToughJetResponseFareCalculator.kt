package org.deblock.exercise.infrastructure.adapter.out.rest.toughjet

import org.deblock.exercise.domain.model.Money
import java.math.BigDecimal

object ToughJetResponseFareCalculator {

    private val oneHundred = BigDecimal.valueOf(100)

    fun calculateTotalFare(response: ToughJetResponse): Money = with(response) {
        val discountPercentage = BigDecimal(discount)
        val priceWithDiscount = basePrice
            .multiply(oneHundred.minus(discountPercentage))
            .divide(oneHundred)
        return priceWithDiscount.add(tax)
    }
}