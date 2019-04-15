package proglife.fora.bank.ui.finances.history

import proglife.fora.bank.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface FinanceHistoryView: BaseView {
    fun showItems(list: List<Any>)
}