package proglife.fora.bank.ui.auth.reg

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import proglife.fora.bank.features.card.CardUtils
import proglife.fora.bank.features.card.models.CardBank
import proglife.fora.bank.features.card.models.CardBrand
import proglife.fora.bank.ui.auth.reg.models.CardFM
import proglife.fora.bank.utils.error.Bag

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@StateStrategyType(OneExecutionStateStrategy::class)
interface RegView: MvpView {
    fun showCompletedCard(cardInfo: Pair<CardFM, Pair<CardBrand?, CardBank?>>)
    fun showCredentialsForm()
    fun showPasswordStrength(strengthLevel: Int)
    fun showSecuritySettings()
    fun showRegForm()
    fun showCardValidationError(bag: Bag)
    fun showRegValidationError(bag: Bag)
    fun showCode(code: Int)
}