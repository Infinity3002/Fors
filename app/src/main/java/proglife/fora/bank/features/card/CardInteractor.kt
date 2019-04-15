package proglife.fora.bank.features.card

import io.reactivex.Single
import proglife.fora.bank.features.card.models.Card
import proglife.fora.bank.features.card.models.CardBank
import proglife.fora.bank.features.card.models.CardBrand
import proglife.fora.bank.features.card.models.CardWithInfo
import proglife.fora.bank.ui.auth.reg.models.CardFM

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CardInteractor(
        private val cardRepository: CardRepository,
        private val cardUtils: CardUtils,
        private val cardValidator: CardValidator
) {

    fun validateCard(cardFM: CardFM): Single<CardFM> {
        return cardValidator.validateCard(cardFM).rx(cardFM)
    }

    fun getCardInfo(cardNumber: String): Single<Pair<CardBrand?, CardBank?>> {
        return Single.fromCallable {
            val cardBrand = cardUtils.findCardBrand(cardNumber)
            val cardInfo = cardUtils.findCardBank(cardNumber)
            Pair(cardBrand, cardInfo)
        }
    }

    fun loadCards(): Single<List<CardWithInfo>> {
        return cardRepository.getCards()
                .toObservable()
                .flatMapIterable { it }
                .flatMap { card ->
                    getCardInfo(card.number)
                            .map { CardWithInfo(card, it.first, it.second) }
                            .toObservable()
                }
                .toList()
    }

}