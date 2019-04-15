package proglife.fora.bank.features.card

import proglife.fora.bank.ui.auth.reg.models.CardFM
import proglife.fora.bank.utils.error.Bag

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CardValidator {

    companion object {
        const val CARD_NUMBER = "card_number"
        const val CARD_MONTH_EXP = "card_month_exp"
        const val CARD_YEAR_EXP = "card_year_exp"
        const val CARD_CVV = "card_cvv"
    }

    fun validateCard(cardFM: CardFM): Bag {
        val bag = Bag("Invalid credit card")
        if (validCardNumber(cardFM.number).not()) bag.add(CARD_NUMBER, "")
        if (validCardMonth(cardFM.monthExp).not()) bag.add(CARD_MONTH_EXP, "")
        if (validCardYear(cardFM.yearExp).not()) bag.add(CARD_YEAR_EXP, "")
        //if (validCardCvv(cardFM.cvv).not()) bag.add(CARD_CVV, "")
        return bag
    }

    private fun validCardNumber(cardNumber: String): Boolean {
        return cardNumber.matches(Regex("^\\d{16}$")) && luhnCheck(cardNumber)
    }

    private fun validCardMonth(month: String): Boolean {
        return month.matches(Regex("^\\d{2}$")) && month.toInt() in 1..12
    }

    private fun validCardYear(year: String): Boolean {
        return year.matches(Regex("^\\d{2}$"))
    }

    private fun validCardCvv(cvv: String): Boolean {
        return cvv.matches(Regex("^\\d{3}$"))
    }

    /**
     * Checks for a valid credit card number.
     * @param cardNumber Credit Card Number.
     * @return Whether the card number passes the luhnCheck.
     */
    private fun luhnCheck(cardNumber: String): Boolean {
        val digits = cardNumber.length
        val oddOrEven = digits and 1
        var sum: Long = 0
        for (count in 0 until digits) {
            var digit = 0
            try {
                digit = Integer.parseInt(cardNumber[count] + "")
            } catch (e: NumberFormatException) {
                return false
            }

            if (count and 1 xor oddOrEven == 0) { // not
                digit *= 2
                if (digit > 9) {
                    digit -= 9
                }
            }
            sum += digit.toLong()
        }

        return if (sum == 0L) false else sum % 10 == 0L
    }

}