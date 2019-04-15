package proglife.fora.bank.ui.finances.cards.detail

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import proglife.fora.bank.features.card.models.CardWithInfo
import proglife.fora.bank.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 19.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface CardDetailView: BaseView {
    fun showCard(cardWithInfo: CardWithInfo)
}