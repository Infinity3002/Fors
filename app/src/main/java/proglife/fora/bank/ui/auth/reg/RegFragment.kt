package proglife.fora.bank.ui.auth.reg

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.arellomobile.mvp.presenter.InjectPresenter
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.fragment_auth_reg.*
import kotlinx.android.synthetic.main.fragment_auth_reg.view.*
import kotlinx.android.synthetic.main.view_reg_empty_card.*
import proglife.fora.bank.R
import proglife.fora.bank.features.card.models.CardBank
import proglife.fora.bank.features.card.models.CardBrand
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.auth.reg.models.CardFM
import proglife.fora.bank.ui.auth.reg.models.CredentialsFM
import proglife.fora.bank.ui.auth.reg.views.RegCredentialsView
import proglife.fora.bank.ui.auth.reg.views.RegFormView
import proglife.fora.bank.ui.auth.reg.views.RegSecurityView
import proglife.fora.bank.ui.auth.reg.views.RegSmsView
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.utils.AnimationDirection
import proglife.fora.bank.utils.NotificationUtils
import proglife.fora.bank.utils.OnBackPressedListener
import proglife.fora.bank.utils.error.Bag


/**
 * Created by Evhenyi Shcherbyna on 11.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class RegFragment : BaseFragment(), RegView, OnBackPressedListener {

    companion object {
        const val FORM_POSITION = 0
        const val CREDENTIALS_POSITION = 1
        const val SMS_POSITION = 2
        const val SECURITY_POSITION = 3

        const val SCAN_REQUEST_CODE = 17500
    }

    @InjectPresenter
    lateinit var mPresenter: RegPresenter
    lateinit var formView: RegFormView
    lateinit var smsView: RegSmsView
    lateinit var credentialsView: RegCredentialsView
    lateinit var securityView: RegSecurityView

    var blocked = false
    var selectpage: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth_reg, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        formView = view.regFormView.init(mvpDelegate, object : RegFormView.OnFormActionListener {
            override fun onCardConfirmed() {

                startNextAnimation(FORM_POSITION)
            }
            override fun onCardPrepare(cardFM: CardFM) {
                mPresenter.processCard(cardFM)
            }
        })
        smsView = view.regSmsView.init(mvpDelegate, object : RegSmsView.OnSMSListener{
            override fun onCheckSMS(code: String) {
                mPresenter.checkSmsCode(code)
            }

            override fun resend() {
                mPresenter.resend()
            }

        })
        credentialsView = view.regCredentialsView.init(mvpDelegate, object : RegCredentialsView.OnCredentialsActionListener {
            override fun onCheckCredentials(credentialsFM: CredentialsFM) {
                mPresenter.confirmCredentials(credentialsFM)
            }

            override fun onCheckPasswordStrength(password: String) {
                mPresenter.checkPasswordStrength(password)
            }
        })
        securityView = view.regSecurityView.init(mvpDelegate) {
            when (it) {
                RegSecurityView.NEXT_CLICK -> mPresenter.regComplete()
                RegSecurityView.TOUCH_ID_CLICK -> (activity as MainActivity).router.navigateTo(Screens.TOUCH_ID)
            }
        }

        btnBack.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_back_button))
        btnBack.setOnClickListener { activity?.onBackPressed() }

        (activity as MainActivity).bottomBarState(true)

        if (selectpage != 0) {
            stepView.selectPage(selectpage + 1, false)
            vfSteps.displayedChild = selectpage
        }

        btnScan.setOnClickListener {
            val scanIntent = Intent(context, CardIOActivity::class.java)
            scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true)
            scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true)
            startActivityForResult(scanIntent, SCAN_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                val scanResult: CreditCard = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)
                formView.onScanResult(scanResult)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun onBackPressed(): Boolean {
        return if (vfSteps.displayedChild == SECURITY_POSITION) {
            mPresenter.regComplete()
            true
        } else if (vfSteps.displayedChild in 1..2) {
            startPreviousAnimation(vfSteps.displayedChild)
            true
        } else {
            formView.onBack()
        }
    }

    private fun startNextAnimation(fromPosition: Int) {
        if (blocked) return
        stepView.selectPage(fromPosition + 2)
        when (fromPosition) {
            FORM_POSITION -> {
                blocked = true
                toolbar.setColors(getBackgroundColor(CREDENTIALS_POSITION))
                formView.startAnimation(true, object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        vfSteps.displayedChild = CREDENTIALS_POSITION
                        selectpage = vfSteps.displayedChild
                        credentialsView.startAnimation(AnimationDirection.FROM_RIGHT, object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                blocked = false
                            }
                        })
                    }
                })
            }
            SMS_POSITION -> {
                btnBack.visibility = View.GONE
                blocked = true
                toolbar.setColors(getBackgroundColor(SECURITY_POSITION))
                smsView.startAnimation(AnimationDirection.TO_LEFT, object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        vfSteps.displayedChild = SECURITY_POSITION
                        selectpage = vfSteps.displayedChild
                        securityView.startAnimation(AnimationDirection.FROM_RIGHT, object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                blocked = false
                            }
                        })
                    }
                })
            }
            CREDENTIALS_POSITION -> {
                blocked = true
                toolbar.setColors(getBackgroundColor(SMS_POSITION))
                credentialsView.startAnimation(AnimationDirection.TO_LEFT, object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        vfSteps.displayedChild = SMS_POSITION
                        selectpage = vfSteps.displayedChild
                        smsView.startAnimation(AnimationDirection.FROM_RIGHT, object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                blocked = false
                            }
                        })
                    }
                })
            }
        }
    }

    private fun startPreviousAnimation(fromPosition: Int) {
        if (blocked) return
        stepView.selectPage(fromPosition)
        when (fromPosition) {
            SMS_POSITION -> {
                blocked = true
                toolbar.setColors(getBackgroundColor(CREDENTIALS_POSITION))
                smsView.startAnimation(AnimationDirection.TO_RIGHT, object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        vfSteps.displayedChild = CREDENTIALS_POSITION
                        selectpage = vfSteps.displayedChild
                        credentialsView.startAnimation(AnimationDirection.FROM_LEFT, object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                blocked = false
                            }
                        })
                    }
                })
            }
            CREDENTIALS_POSITION -> {
                blocked = true
                toolbar.setColors(getBackgroundColor(FORM_POSITION))
                credentialsView.startAnimation(AnimationDirection.TO_RIGHT, object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        vfSteps.displayedChild = FORM_POSITION
                        selectpage = vfSteps.displayedChild
                        formView.startAnimation(false, object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                credentialsView.clear()
                                blocked = false
                            }
                        })
                    }
                })
            }
            SECURITY_POSITION -> {
                blocked = true
                toolbar.setColors(getBackgroundColor(SMS_POSITION))
                securityView.startAnimation(AnimationDirection.TO_RIGHT, object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        vfSteps.displayedChild = SMS_POSITION
                        selectpage = vfSteps.displayedChild
                        smsView.startAnimation(AnimationDirection.FROM_LEFT, object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                blocked = false
                            }
                        })
                    }
                })
            }
        }
    }

    override fun showCompletedCard(cardInfo: Pair<CardFM, Pair<CardBrand?, CardBank?>>) {
        formView.showCard(cardInfo)
    }

    override fun showCredentialsForm() {
        smsView.clearCode()
        startNextAnimation(SMS_POSITION)
    }

    override fun showPasswordStrength(strengthLevel: Int) {
        credentialsView.showPasswordStrength(strengthLevel)
    }

    override fun showSecuritySettings() {
        startNextAnimation(SMS_POSITION)
    }

    override fun showRegForm() {
        startNextAnimation(CREDENTIALS_POSITION)
    }


    override fun showCardValidationError(bag: Bag) {
        formView.showValidationError(bag)
    }

    override fun showRegValidationError(bag: Bag) {
        credentialsView.showRegValidationError(bag)
    }

    private fun getBackgroundColor(page: Int): IntArray {
        val res = when (page) {
            FORM_POSITION ->  R.color._ed4949 to R.color._f1b074
            SMS_POSITION -> R.color._f1b074 to R.color._ed8353
            CREDENTIALS_POSITION -> R.color._ed8353 to R.color._ed4949
            SECURITY_POSITION -> R.color._ed4949 to R.color._ba3753
            else -> 0 to 0
        }
        return intArrayOf(ContextCompat.getColor(context!!, res.first), ContextCompat.getColor(context!!, res.second))
    }

    override fun showCode(code: Int) {
        //TODO смс код
        NotificationUtils.showNotifi(context!!, message = "Code: $code")
    }
}