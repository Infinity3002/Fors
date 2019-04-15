package proglife.fora.bank.ui.finances.cards.detail

import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.features.card.CardUtils
import proglife.fora.bank.features.card.di.CardComponent
import proglife.fora.bank.features.card.models.Card
import proglife.fora.bank.features.card.models.CardWithInfo
import proglife.fora.bank.ui.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 19.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class CardDetailPresenter(private val cardWithInfo: CardWithInfo):
        BasePresenter<CardDetailView>() {

    @Inject
    lateinit var mRouter: Router

    init {
        CardComponent.Initializer.init().inject(this)
    }

    fun back() {
        mRouter.exit()
    }

    init {
        viewState.showCard(cardWithInfo)
    }

}