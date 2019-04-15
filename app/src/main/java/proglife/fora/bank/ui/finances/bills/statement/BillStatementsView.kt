package proglife.fora.bank.ui.finances.bills.statement

import proglife.fora.bank.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface BillStatementsView : BaseView {
    fun showItems(list: List<Any>)
}