package proglife.fora.bank.ui.finances.history

import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.App
import proglife.fora.bank.features.finance.models.History
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class FinanceHistoryPresenter: BasePresenter<FinanceHistoryView>() {

    @Inject
    lateinit var mRouter: Router

    init {
        App.appComponent.inject(this)
        viewState.showItems(listOf(
                HistoryGroup("Вчера, 5 сентября", "+560,15 \u20BD"),
                History("Капитализация средств", "Внутрибанковские операции", "5560,15 \u20BD",
                        "http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png"),
                History("Капитализация средств", "Внутрибанковские операции", "5560,15 \u20BD",
                        "http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png"),
                History("Капитализация средств", "Внутрибанковские операции", "5560,15 \u20BD",
                        "http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png"),
                HistoryGroup("4 сентября", "+6567,07 \u20BD"),
                History("Капитализация средств", "Внутрибанковские операции", "5560,15 \u20BD",
                        "http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png"),
                History("Капитализация средств", "Внутрибанковские операции", "5560,15 \u20BD",
                        "http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png"),
                History("Капитализация средств", "Внутрибанковские операции", "5560,15 \u20BD",
                        "http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png")
        ))
    }

    fun select(history: History) {
        mRouter.navigateTo(Screens.HISTORY_DETAIL)
    }

}