package proglife.fora.bank.ui.finances.statistic

import org.threeten.bp.LocalDate
import proglife.fora.bank.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface FinanceStatisticView: BaseView {
    fun showMonth(date: LocalDate)
}