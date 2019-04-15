package proglife.fora.bank.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.bottom_toolbar.view.*
import proglife.fora.bank.R
import proglife.fora.bank.navigation.AppRouter
import proglife.fora.bank.navigation.Screens.FEED
import proglife.fora.bank.navigation.Screens.FINANCES
import proglife.fora.bank.navigation.Screens.NEED_AUTH
import proglife.fora.bank.navigation.Screens.PROFILE
import proglife.fora.bank.navigation.Screens.SERVICES
import proglife.fora.bank.navigation.Screens.TRANSFERS
import ru.terrakok.cicerone.Router

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BottomToolbarView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val dividerPaint = Paint()

    init {
        dividerPaint.color = ContextCompat.getColor(context, R.color.divider_color)
        dividerPaint.strokeWidth = context.resources.getDimension(R.dimen._1sdp)
    }

    fun initNavigation(router: Router, authorized: Boolean) {
        val set = ConstraintSet()
        set.clone(this)

        val buttons = if (authorized) {
            set.setVisibility(btnTransfers.id, View.VISIBLE)
            set.setVisibility(btnCards.id, View.VISIBLE)
            set.setHorizontalChainStyle(btnFeed.id, ConstraintSet.CHAIN_SPREAD_INSIDE)

            mapOf<String, View>(
                    FEED to btnFeed,
                    PROFILE to btnProfile,
                    TRANSFERS to btnTransfers,
                    SERVICES to btnServices,
                    FINANCES to btnCards
            )
        } else {
            set.setHorizontalChainStyle(btnFeed.id, ConstraintSet.CHAIN_SPREAD)
            set.setVisibility(btnTransfers.id, View.GONE)
            set.setVisibility(btnCards.id, View.GONE)

            mapOf<String, View>(
                    FEED to btnFeed,
                    NEED_AUTH to btnProfile,
                    SERVICES to btnServices
            )
        }

//        TransitionManager.beginDelayedTransition(bottomToolbar as ViewGroup)
        set.applyTo(this)

        buttons.forEach { entry ->
            entry.value.setOnClickListener {
                selectScreen(buttons, it, router, entry.key)
            }
        }

        (router as AppRouter).onNewRootScreenListener = {
            selectButton(buttons, buttons[it])
        }
    }

    private fun selectScreen(buttons: Map<String, View>, pressedButton: View, router: Router, screenKey: String) {
        if (!selectButton(buttons, pressedButton)) return
        router.newRootScreen(screenKey)
    }

    private fun selectButton(buttons: Map<String, View>, pressedButton: View?): Boolean {
        if (pressedButton?.isSelected == true) return false // Already selected
        buttons.forEach { it.value.isSelected = it.value == pressedButton }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, dividerPaint)
    }

}