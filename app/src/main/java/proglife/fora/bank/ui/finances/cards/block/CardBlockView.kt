package proglife.fora.bank.ui.finances.cards.block

import proglife.fora.bank.features.card.models.CardWithInfo
import proglife.fora.bank.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 23.08.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface CardBlockView : BaseView {
    fun showCard(cardWithInfo: CardWithInfo)
}