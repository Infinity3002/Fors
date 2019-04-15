package proglife.fora.bank.features.card

import android.content.Context
import org.json.JSONArray
import proglife.fora.bank.features.card.models.CardBank
import proglife.fora.bank.features.card.models.CardBrand

/**
 * Created by Evhenyi Shcherbyna on 15.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CardUtils(context: Context) {

    companion object {
        const val BRANDS_PATH = "brands-logos/"
        const val BANKS_PATH = "banks-logos/"

        private const val FILE_WITH_BANKS = "banks.json"

        fun maskCard(cardNumber: String): String {
            return "**** **** **** ${cardNumber.substring(cardNumber.lastIndex - 3..cardNumber.lastIndex)}"
        }
    }

    private val banks: List<CardBank> by lazy {
        val text = context.assets.open(FILE_WITH_BANKS).bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(text)
        val items = mutableListOf<CardBank>()
        for (i in 0 until jsonArray.length()) {
            try {
                val jsonObject = jsonArray.getJSONObject(i)
                val item = CardBank(
                        jsonObject.getString("name"),
                        jsonObject.getString("nameEn"),
                        jsonObject.getString("alias"),
                        parseStringSet(jsonObject.getJSONArray("backgroundColors")),
                        jsonObject.getString("text"),
                        parseStringSet(jsonObject.getJSONArray("prefixes"))
                )
                items.add(item)
            } catch (t : Throwable) {
                t.printStackTrace()
            }
        }
        items
    }

    fun findCardBank(cardNumber: String): CardBank? {
        for (bankInfo in banks) {
            for (prefix in bankInfo.prefixes) {
                if (cardNumber.startsWith(prefix)) return bankInfo
            }
        }
        return null
    }

    fun findCardBrand(cardNumber: String): CardBrand? {
        for (cardBrand in CardBrand.values()) {
            if (cardNumber.matches(cardBrand.pattern)) return cardBrand
        }
        return null
    }

    private fun parseStringSet(jsonArray: JSONArray): Set<String> {
        val items = mutableSetOf<String>()
        for (i in 0 until jsonArray.length()) {
            items.add(jsonArray.getString(i))
        }
        return items
    }

}