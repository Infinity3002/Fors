package proglife.fora.bank.ui.finances.history.detail

import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.App
import proglife.fora.bank.ui.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 04.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class HistoryDetailPresenter : BasePresenter<HistoryDetailView>() {

    @Inject
    lateinit var mRouter: Router

    init {
        App.appComponent.inject(this)
    }

    fun back() {
        mRouter.exit()
    }

}