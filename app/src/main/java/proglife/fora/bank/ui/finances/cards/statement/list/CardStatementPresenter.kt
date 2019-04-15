package proglife.fora.bank.ui.finances.cards.statement.list

import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.App
import proglife.fora.bank.models.Statement
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 04.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class CardStatementPresenter: BasePresenter<CardStatementView>() {

    @Inject
    lateinit var mRouter: Router

    init {
        App.appComponent.inject(this)
    }

    fun select(statement: Statement) {
        mRouter.navigateTo(Screens.STATEMENT_DETAILS)
    }

}