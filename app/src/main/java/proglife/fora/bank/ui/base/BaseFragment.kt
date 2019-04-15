package proglife.fora.bank.ui.base

import android.view.View
import com.arellomobile.mvp.MvpAppCompatFragment
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.widgets.arc.ArcTrait
import proglife.fora.bank.widgets.arc.ArcView

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
abstract class BaseFragment : MvpAppCompatFragment(), ArcTrait.HasArcView {

    fun bottomBarState(hide : Boolean) {
        (context as MainActivity).bottomBarState(hide)
    }

    override fun getArcView(): View? {
        return ArcTrait.findArcView(view)
    }

}