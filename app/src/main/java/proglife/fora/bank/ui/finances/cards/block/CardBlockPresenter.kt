package proglife.fora.bank.ui.finances.cards.block

import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.App
import proglife.fora.bank.features.card.models.CardWithInfo
import proglife.fora.bank.ui.base.BasePresenter
import proglife.fora.bank.ui.finances.cards.list.CardsPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 23.08.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class CardBlockPresenter(private val cardWithInfo: CardWithInfo) : BasePresenter<CardBlockView>() {

    @Inject
    lateinit var mRouter: Router

    init {
        App.appComponent.inject(this)
        viewState.showCard(cardWithInfo)
    }

    fun lock() {
        val value = cardWithInfo.copy(card = cardWithInfo.card.copy(locked = true))
        mRouter.exitWithResult(CardsPresenter.CHANGE_CARD_REQUEST_CODE, value)
    }

}