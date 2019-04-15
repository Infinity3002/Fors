package proglife.fora.bank.ui.finances.bills

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.presenter.InjectPresenter
import proglife.fora.bank.App
import proglife.fora.bank.features.bills.models.Bill
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class BillsPresenter: BasePresenter<BillsView>() {

    @Inject
    lateinit var mRouter: Router

    init {
        App.appComponent.inject(this)
        viewState.showBills(listOf(
                Bill("Зарплатный счет", "2246", 39500.0),
                Bill("Моя сберочка", "1110", 55560.0),
                Bill("Сейф", "3447", 23543.0),
                Bill("Вклад \"Комфортный\"", "9061", 118556.0),
                Bill("Бонусный счет", "1106", 5500.0)
        ))
    }

    fun addBill() {

    }

    fun sendBill(bill: Bill) {
        mRouter.navigateTo(Screens.CHAT_REQUEST, Screens.FINANCES)
    }

    fun refillBill(bill: Bill) {
        mRouter.navigateTo(Screens.REFILL_REQUEST, Screens.FINANCES)
    }

    fun selectBill(bill: Bill) {
        mRouter.navigateTo(Screens.BILL_DETAIL, bill)
    }

}