package proglife.fora.bank.ui.finances.bonds

import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.features.bonds.models.Bond
import proglife.fora.bank.ui.base.BasePresenter

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class BondsPresenter: BasePresenter<BondsView>() {

    init {
        viewState.showBonds(listOf(
                Bond("АФК Система, Sistema-19", "Предложений по бумаге нет",
                        8.83f,
                        "http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png"),
                Bond("Газпром, GAZPR-37", "Предложений по бумаге нет",
                        5.37f,
                        "http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png"),
                Bond("ВЭБ, VEB-23", "Предложений по бумаге нет",
                        4.04f,
                        "http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png"),
                Bond("Роснефть, RosNef-22", "Гипермаркет",
                        3.84f,
                        "http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png")
        ))
    }

    fun searchBond(text: String) {

    }

    fun buyBond() {

    }

    fun selectBond(bond: Bond) {

    }

}