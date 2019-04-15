package proglife.fora.bank.ui.finances.cards.list

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.features.card.CardInteractor
import proglife.fora.bank.features.card.CardUtils
import proglife.fora.bank.features.card.di.CardComponent
import proglife.fora.bank.features.card.models.Card
import proglife.fora.bank.features.card.models.CardWithInfo
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 19.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class CardsPresenter : BasePresenter<CardsView>() {

    companion object {
        const val CHANGE_CARD_REQUEST_CODE = 10000
    }

    @Inject
    lateinit var mCardInteractor: CardInteractor
    @Inject
    lateinit var mRouter: Router

    private var cards: MutableList<CardWithInfo>? = null

    init {
        CardComponent.Initializer.init().inject(this)
        mRouter.setResultListener(CHANGE_CARD_REQUEST_CODE) { card ->
            cards?.forEachIndexed { index, cardWithInfo ->
                if (cardWithInfo.card.id == (card as CardWithInfo).card.id) {
                    cards!![index] = card
                    return@forEachIndexed
                }
            }
            viewState.updateCard(card as CardWithInfo)
        }
    }

    fun select(cardWithInfo: CardWithInfo) {
        mRouter.navigateTo(Screens.CARDS_DETAIL, cardWithInfo)
    }

    fun moveToLast(cardWithInfo: CardWithInfo) {
        cards?.let { c ->
            c.find { it.card == cardWithInfo.card }?.let {
                c.remove(cardWithInfo)
                c.add(cardWithInfo)
            }
        }
    }

    @SuppressLint("CheckResult")
    fun loadCards() {
        if (cards == null) {
            mCardInteractor.loadCards()
                    .compose(applySingleSchedulers())
                    .doOnSubscribe { viewState.showCardsLoading() }
                    .doFinally { viewState.dismissCardsLoading() }
                    .subscribe(
                            {
                                cards = it.toMutableList()
                                viewState.showCards(it)
                            },
                            {

                            }
                    )
        } else {
            viewState.showCards(cards!!)
        }
    }

    fun block(cardWithInfo: CardWithInfo) {
        mRouter.navigateTo(Screens.CARD_BLOCK, cardWithInfo)
    }

    override fun onDestroy() {
        mRouter.removeResultListener(CHANGE_CARD_REQUEST_CODE)
        super.onDestroy()
    }
}