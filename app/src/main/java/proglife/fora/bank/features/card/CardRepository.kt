package proglife.fora.bank.features.card

import io.reactivex.Single
import proglife.fora.bank.features.card.models.Card

/**
 * Created by Evhenyi Shcherbyna on 19.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface CardRepository {
    fun getCards(): Single<List<Card>>
}

class CardRepositoryTest : CardRepository {

    override fun getCards(): Single<List<Card>> {
        return Single.fromCallable {
            listOf(
                    Card(1, "Дебетовая", 7500.25, "5559567898453478", "04", "21", false),
                    Card(2,"Сбербанк", 15000.0, "4276802016632745", "04", "21", false),
                    Card(3,"Visa Gold Cashback", 7000.0, "2200303412341234", "04", "21", false),
                    Card(4,"Дебетовая", 3500.15, "437773000000123", "04", "21", false),
                    Card(5,"Дебетовая", 1500.15, "558334000000123", "04", "21", false),
                    Card(6,"Дебетовая", 25000.50, "677585000000123", "04", "21", false)
            )
        }
    }

}