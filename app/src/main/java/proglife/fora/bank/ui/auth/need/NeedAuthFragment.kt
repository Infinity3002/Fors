package proglife.fora.bank.ui.auth.need

import android.app.Activity
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_need_auth.*
import proglife.fora.bank.R
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.main.MainActivity

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class NeedAuthFragment : BaseFragment(), NeedAuthView {

    @InjectPresenter
    lateinit var mPresenter: NeedAuthPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_need_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnReg.setOnClickListener {
            (activity as MainActivity).router.navigateTo(Screens.REG)
        }
        btnLogin.setOnClickListener {
            (activity as MainActivity).router.navigateTo(Screens.LOGIN)
        }

        ivBackground.scaleX = 1.1f

        val size = Point()
        (context as Activity).windowManager.defaultDisplay.getSize(size)

        ivBackground.translationX = size.x * 0.05f
        val animateBackground = ivBackground.animate()
        animateBackground.translationXBy(-size.x * 0.1f)
        animateBackground.duration = 3000
        animateBackground.interpolator = LinearOutSlowInInterpolator()
        animateBackground.startDelay = 500
        animateBackground.start()

        val animateOverlay = overlay.animate().alpha(0.9f)
        animateOverlay.duration = 2000
        animateOverlay.start()

        (activity as MainActivity).bottomBarState(false)
    }
//
//    fun getTransitiveView(): View{
//        return overlay
//    }

//    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator {
//        return super.onCreateAnimator(transit, enter, nextAnim)
//    }
}