package proglife.fora.bank.ui.main

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import proglife.fora.bank.App
import proglife.fora.bank.features.auth.AuthInteractor
import proglife.fora.bank.navigation.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 20.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@SuppressLint("CheckResult")
@InjectViewState
class MainPresenter: MvpPresenter<MainView>() {

    @Inject
    lateinit var mAuthInteractor: AuthInteractor
    @Inject
    lateinit var mRouter: Router

    private val mComposeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
    }

    fun init() {
        mAuthInteractor.isAuthenticated()
                .doOnSubscribe { mComposeDisposable.add(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewState.onAuthenticationChanged(it)
                    if (!it) mRouter.newRootScreen(Screens.NEED_AUTH)
                }
    }

}