package proglife.fora.bank.ui.main

import com.arellomobile.mvp.MvpView

/**
 * Created by Evhenyi Shcherbyna on 20.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface MainView: MvpView {
    fun onAuthenticationChanged(authorized: Boolean)
}