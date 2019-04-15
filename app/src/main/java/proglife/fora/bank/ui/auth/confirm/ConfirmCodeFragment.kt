package proglife.fora.bank.ui.auth.confirm

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_auth_reg_sms.*
import kotlinx.android.synthetic.main.fragment_auth_reg_sms.view.*
import kotlinx.android.synthetic.main.fragment_confirm_sms.*
import proglife.fora.bank.R
import proglife.fora.bank.features.auth.models.LoginInfo
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.utils.DefaultTextWather

/**
 * Created by Evhenyi Shcherbyna on 25.09.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ConfirmCodeFragment : BaseFragment(), ConfirmCodeView {

    companion object {
        private const val DATA = "data"

        fun newInstance(loginInfo: LoginInfo): ConfirmCodeFragment {
            val bundle = Bundle()
            bundle.putParcelable(DATA, loginInfo)
            val fragment = ConfirmCodeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mPresenter: ConfirmCodePresenter

    @ProvidePresenter
    fun providePresenter(): ConfirmCodePresenter {
        return ConfirmCodePresenter(arguments!!.getParcelable(DATA))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_sms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnBack.setOnClickListener {
            mPresenter.back()
        }
        etCode.addTextChangedListener(object : DefaultTextWather(){
            override fun afterTextChanged(p0: Editable?) {
                mPresenter.verifyCode(p0.toString())
            }
        })

        btnRetry.setOnClickListener {
            mPresenter.retry()
        }
    }

    override fun setLoadingState(enable: Boolean) {
        etCode.isEnabled = !enable
    }

}