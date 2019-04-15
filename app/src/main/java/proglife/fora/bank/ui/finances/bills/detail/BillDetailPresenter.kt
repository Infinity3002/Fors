package proglife.fora.bank.ui.finances.bills.detail

import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.App
import proglife.fora.bank.features.bills.models.Bill
import proglife.fora.bank.ui.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class BillDetailPresenter(bill: Bill) : BasePresenter<BillDetailView>() {

    @Inject
    lateinit var mRouter: Router

    init {
        App.appComponent.inject(this)
    }

    fun back() {
        mRouter.exit()
    }
}