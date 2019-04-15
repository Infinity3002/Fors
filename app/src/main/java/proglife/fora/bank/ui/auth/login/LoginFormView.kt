package proglife.fora.bank.ui.auth.login

import com.arellomobile.mvp.MvpView

/**
 * Created by Evhenyi Shcherbyna on 07.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface LoginFormView : MvpView {
    fun showError(text: String)
    fun setLoadingState(enable: Boolean)
}