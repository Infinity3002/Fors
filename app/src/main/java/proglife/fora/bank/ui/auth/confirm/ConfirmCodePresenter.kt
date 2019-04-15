package proglife.fora.bank.ui.auth.confirm

import android.annotation.SuppressLint
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import proglife.fora.bank.App
import proglife.fora.bank.features.auth.AuthInteractor
import proglife.fora.bank.features.auth.models.LoginInfo
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 25.09.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class ConfirmCodePresenter(private val loginInfo: LoginInfo) : BasePresenter<ConfirmCodeView>() {

    @Inject
    lateinit var mRouter: Router
    @Inject
    lateinit var mAuthInteractor: AuthInteractor

    init {
        App.appComponent.inject(this)
    }

    @SuppressLint("CheckResult")
    fun onSuccess() {
        mAuthInteractor.authorize()
                .compose(applySingleSchedulers())
                .subscribe(
                        {
                            mRouter.newRootScreen(Screens.FEED)
                        },
                        {

                        }
                )
    }

    @SuppressLint("CheckResult")
    fun verifyCode(code : String) {
        Log.d("LOGS", code)
        if(code.length  >= 6)
        mAuthInteractor.verifyLoginCode(loginInfo, code)
                .compose(applySingleSchedulers())
                .doOnSubscribe { viewState.setLoadingState(true) }
                .doOnEvent { _, _ -> viewState.setLoadingState(false) }
                .subscribe(
                        {
                            onSuccess()
                        },
                        {
                            it.printStackTrace()
                        }
                )
    }

    @SuppressLint("CheckResult")
    fun retry() {
        mAuthInteractor.login(loginInfo)
                .compose(applySingleSchedulers())
                .subscribe(
                        {

                        },
                        {
                            it.printStackTrace()
                        }
                )
    }

    fun back() {
        mRouter.exit()
    }

}