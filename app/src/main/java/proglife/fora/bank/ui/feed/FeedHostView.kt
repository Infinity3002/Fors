package proglife.fora.bank.ui.feed

import com.arellomobile.mvp.MvpView

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface FeedHostView: MvpView {
    fun init(isAuth: Boolean)
}