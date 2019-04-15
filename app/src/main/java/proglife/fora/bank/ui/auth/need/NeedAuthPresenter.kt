package proglife.fora.bank.ui.auth.need

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpView
import proglife.fora.bank.App
import proglife.fora.bank.features.auth.AuthInteractor
import proglife.fora.bank.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class NeedAuthPresenter : BasePresenter<NeedAuthView>() {

    init {
        App.appComponent.inject(this)
        getToken()
    }

    @Inject
    lateinit var mAuthInteractor: AuthInteractor

    @SuppressLint("CheckResult")
    fun getToken(){
        mAuthInteractor.getToken()
                .compose(applySingleSchedulers())
                .subscribe(
                        {},
                        {
                            it.printStackTrace()
                        }
                )
    }
}