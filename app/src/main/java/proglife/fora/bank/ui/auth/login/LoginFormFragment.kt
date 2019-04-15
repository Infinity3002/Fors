package proglife.fora.bank.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_login_form.*
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.R
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.auth.reg.touchid.TouchIdFragment
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.utils.FingerprintUtil
import proglife.fora.bank.utils.OnBackPressedListener
import proglife.fora.bank.utils.isVisibly

/**
 * Created by Evhenyi Shcherbyna on 07.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class LoginFormFragment : BaseFragment(), LoginFormView, OnBackPressedListener {

    @InjectPresenter
    lateinit var mPresenter: LoginFormPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        etLogin.setText(LoginFormPresenter.TEST_LOGIN)
//        etPassword.setText(LoginFormPresenter.TEST_PASS)
        btnBack.startAnimation(AnimationUtils.loadAnimation(context, R.anim.show_back_button))
        btnBack.setOnClickListener { mPresenter.goNeedAuth() }

        (activity as MainActivity).bottomBarState(true)
        btnNext.setOnClickListener{
            mPresenter.login(etLogin.text.toString(), etPassword.text.toString())
        }
        btnTouchId.setOnClickListener{
            mPresenter.loginWithTouchId()

        }
//        if (FingerprintUtil.checkSensorState(context!!) != FingerprintUtil.SensorState.READY) {
            btnTouchId.visibility = View.GONE
//        }
    }

    override fun onBackPressed(): Boolean {
        mPresenter.goNeedAuth()
        return true
    }

    override fun showError(text: String) {
        Toast.makeText(context!!, text, Toast.LENGTH_LONG).show()
    }

    override fun setLoadingState(enable: Boolean) {
        etLogin.isEnabled = !enable
        etPassword.isEnabled = !enable
        btnTouchId.isEnabled = !enable
        btnNext.isVisibly(!enable)
        pbLoading.isVisibly(enable)
    }

}