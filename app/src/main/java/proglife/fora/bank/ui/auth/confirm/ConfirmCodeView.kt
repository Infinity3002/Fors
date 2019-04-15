package proglife.fora.bank.ui.auth.confirm

import com.arellomobile.mvp.MvpView

/**
 * Created by Evhenyi Shcherbyna on 25.09.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface ConfirmCodeView : MvpView {
    fun setLoadingState(enable: Boolean)
}