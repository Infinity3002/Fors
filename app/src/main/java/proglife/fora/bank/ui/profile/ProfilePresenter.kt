package proglife.fora.bank.ui.profile

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.App
import proglife.fora.bank.features.auth.AuthInteractor
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 22.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class ProfilePresenter : BasePresenter<ProfileView>() {

    @Inject
    lateinit var mRouter: Router
    @Inject
    lateinit var mAuthInteractor: AuthInteractor

    init {
        App.appComponent.inject(this)
    }

    fun chats() {
        mRouter.navigateTo(Screens.CHATS)
    }

    @SuppressLint("CheckResult")
    fun logout() {
        mAuthInteractor.logout()
                .compose(applySingleSchedulers())
                .subscribe(
                        {
//                            mRouter.newRootScreen(Screens.NEED_AUTH)
                        },
                        {}
                )
    }

}