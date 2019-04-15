package proglife.fora.bank.ui.finances.statistic

import com.arellomobile.mvp.InjectViewState
import org.threeten.bp.LocalDate
import proglife.fora.bank.App
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BasePresenter
import proglife.fora.bank.ui.finances.statistic.chart.Bubble
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class FinanceStatisticPresenter: BasePresenter<FinanceStatisticView>() {

    @Inject
    lateinit var mRouter: Router

    init {
        App.appComponent.inject(this)
    }

    fun open(bubble: Bubble) {
        mRouter.navigateTo(Screens.STATEMENT_DETAILS)
    }

    fun selectMonth(date: LocalDate) {
        viewState.showMonth(date)
        Timber.i("Date: $date")
    }

}