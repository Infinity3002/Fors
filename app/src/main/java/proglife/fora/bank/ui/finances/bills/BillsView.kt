package proglife.fora.bank.ui.finances.bills

import proglife.fora.bank.features.bills.models.Bill
import proglife.fora.bank.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface BillsView: BaseView {
    fun showBills(list: List<Bill>)
}