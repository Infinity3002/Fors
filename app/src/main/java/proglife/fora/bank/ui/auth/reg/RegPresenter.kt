package proglife.fora.bank.ui.auth.reg

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.features.auth.AuthInteractor
import proglife.fora.bank.features.card.CardInteractor
import proglife.fora.bank.features.card.di.CardComponent
import proglife.fora.bank.features.card.models.CardBank
import proglife.fora.bank.features.card.models.CardBrand
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.auth.reg.models.CardFM
import proglife.fora.bank.ui.auth.reg.models.CredentialsFM
import proglife.fora.bank.ui.base.BasePresenter
import proglife.fora.bank.utils.error.ValidationException
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class RegPresenter : BasePresenter<RegView>() {

    @Inject
    lateinit var mAuthInteractor: AuthInteractor
    @Inject
    lateinit var mCardInteractor: CardInteractor
    @Inject
    lateinit var mRouter: Router

    init {
        CardComponent.Initializer.init().inject(this)
    }

    private var mLastValidCard: Pair<CardFM, Pair<CardBrand?, CardBank?>>? = null

    private var credentialsFM: CredentialsFM? = null

    @SuppressLint("CheckResult")
    fun processCard(cardFM: CardFM) {
        mCardInteractor.validateCard(cardFM)
                .flatMap { card ->
                    mCardInteractor.getCardInfo(card.number)
                            .map { Pair(card, it) }
                }
                .compose(applySingleSchedulers())
                .subscribe(
                        {
                            mLastValidCard = it
                            viewState.showCompletedCard(it)
                        },
                        {
                            if (it is ValidationException) {
                                Timber.i(it.toString())
                                viewState.showCardValidationError(it.bag)
                            }
                        }
                )

    }

    fun checkSmsCode(smsCode: String) {
        if (smsCode.length == 6) {
            registration(smsCode)
        }
    }

    @SuppressLint("CheckResult")
    fun checkPasswordStrength(password: String) {
        mAuthInteractor.getPasswordStrength(password)
                .doOnSubscribe(this::unSubscribeOnDestroy)
                .subscribe(
                        {
                            viewState.showPasswordStrength(it)
                        },
                        {
                            Timber.i(it)
                        }
                )
    }

    @SuppressLint("CheckResult")
    fun confirmCredentials(credentialsFM: CredentialsFM) {
        mAuthInteractor.checkClient(credentialsFM, mLastValidCard!!.first)
                .compose(applySingleSchedulers())
                .subscribe(
                        {
                            this.credentialsFM = credentialsFM
                           viewState.showRegForm()
                           /* if (it != null)
                                viewState.showCode(it)*/
                        },
                        {
                            if (it is ValidationException) {
                                Timber.i(it.toString())
                                viewState.showRegValidationError(it.bag)
                            } else
                                Timber.i(it)

                        }
                )
    }

    @SuppressLint("CheckResult")
    fun regComplete() {
        mAuthInteractor.authorize()
                .compose(applySingleSchedulers())
                .subscribe(
                        {
                            mRouter.newRootScreen(Screens.REG_COMPLETE)
                        },
                        {
                            Timber.i(it)
                        }
                )
    }
    @SuppressLint("CheckResult")
    fun registration(code: String){
        mAuthInteractor.restration(credentialsFM!!, mLastValidCard!!.first, code.toInt())
                .compose(applySingleSchedulers())
                .subscribe(
                        {
                            viewState.showSecuritySettings()
                        },
                        {
                            Timber.i(it)
                        }
                )
    }

    fun resend() {
        confirmCredentials(credentialsFM!!)
    }

}