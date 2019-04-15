package proglife.fora.bank.navigation

import ru.terrakok.cicerone.Router

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class AppRouter : Router() {

    var onNewRootScreenListener: ((String?) -> Unit)? = null

    override fun newRootScreen(screenKey: String?) {
        super.newRootScreen(screenKey)
        onNewRootScreenListener?.invoke(screenKey)
    }

}