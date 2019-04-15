package proglife.fora.bank.utils

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.ViewGroup
import proglife.fora.bank.widgets.arc.ArcToolbarView
import android.support.transition.Transition
import android.support.transition.TransitionValues
import proglife.fora.bank.widgets.arc.ArcView


/**
 * Created by Evhenyi Shcherbyna on 13.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ChangeColorTransition: Transition() {

    companion object {
        private const val PROPNAME_BACKGROUND = "android:factorTransition:bounds"
    }

    private val sTransitionProperties = arrayOf(PROPNAME_BACKGROUND)

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureValues(transitionValues)
    }

    private fun captureValues(values: TransitionValues) {
        val view = values.view
        if (view is ArcView) {
            values.values[PROPNAME_BACKGROUND] = view.getColors()
        }
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
        if (null == startValues || null == endValues) {
            return null
        }

        val view = endValues.view as? ArcView ?: return null

        val startBackground = startValues.values[PROPNAME_BACKGROUND] as IntArray
        val endBackground = endValues.values[PROPNAME_BACKGROUND] as IntArray

        val argbEvaluator = ArgbEvaluator()
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener {
            val startColor = argbEvaluator.evaluate(it.animatedFraction, startBackground[0], endBackground[0]) as Int
            val endColor = argbEvaluator.evaluate(it.animatedFraction, startBackground[1], endBackground[1]) as Int
            view.setColors(intArrayOf(startColor, endColor))
        }
        return animator
    }

    override fun getTransitionProperties(): Array<String> {
        return sTransitionProperties
    }
}