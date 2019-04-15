package proglife.fora.bank.ui.auth.login

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import proglife.fora.bank.App
import proglife.fora.bank.extensions.network.ApiResponse
import proglife.fora.bank.features.auth.AuthInteractor
import proglife.fora.bank.features.auth.models.LoginInfo
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.auth.reg.touchid.TouchIdFragment
import proglife.fora.bank.ui.base.BasePresenter
import proglife.fora.bank.utils.fromJson
import retrofit2.HttpException
import ru.terrakok.cicerone.Router
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 07.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class LoginFormPresenter : BasePresenter<LoginFormView>() {

    companion object {
        const val TEST_LOGIN = "testMen"
        const val TEST_PASS = "testMen"
    }

    @Inject
    lateinit var mRouter: Router
    @Inject
    lateinit var mAuthInteractor: AuthInteractor

    init {
        App.appComponent.inject(this)
        mRouter.setResultListener(TouchIdFragment.RESULT_CODE) {
//            if (TouchIdFragment.RESULT_OK == it) onLoginSuccess()
        }
    }


    fun loginWithTouchId() {
        mRouter.navigateTo(Screens.TOUCH_ID)
    }

    override fun onDestroy() {
        mRouter.removeResultListener(TouchIdFragment.RESULT_CODE)
        super.onDestroy()
    }

    @SuppressLint("CheckResult")
    fun login(login: String, password: String) {
        mAuthInteractor
                .login(LoginInfo(
                        "AND",
                        false,
                        login,
                        password
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    this.unSubscribeOnDestroy(it)
                    viewState.setLoadingState(true)
                }
                .doFinally { viewState.setLoadingState(false) }
                .subscribe(
                        {
                            mRouter.navigateTo(Screens.LOGIN_CONFIRM_CODE, LoginInfo(
                                    "AND",
                                    false,
                                    login,
                                    password
                            ))
                        },
                        {t ->
                            when (t) {
                                is HttpException -> {
                                    when (t.code()) {
                                        500 -> viewState.showError("Ошибка сервера")
                                        else -> viewState.showError(t.fromJson<ApiResponse<Unit>>(Gson()).errorMessage)
                                    }
                                }
                                is IOException -> viewState.showError("Ошибка соединения")
                                else -> viewState.showError("Неизвестная ошибка")
                            }
                        }
                )
    }

    fun goNeedAuth() {
        mRouter.newRootScreen(Screens.NEED_AUTH)
    }

}