package proglife.fora.bank.ui.feed

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import proglife.fora.bank.App
import proglife.fora.bank.features.auth.AuthInteractor
import proglife.fora.bank.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
@SuppressLint("CheckResult")
class FeedHostPresenter: BasePresenter<FeedHostView>() {

    @Inject
    lateinit var mAuthInteractor: AuthInteractor

    init {
        App.appComponent.inject(this)
        mAuthInteractor.isAuthenticated()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewState.init(it)
                }
    }

}