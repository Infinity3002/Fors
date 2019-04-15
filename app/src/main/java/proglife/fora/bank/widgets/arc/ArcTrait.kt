package proglife.fora.bank.widgets.arc

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.*
import android.view.View
import android.view.ViewGroup
import proglife.fora.bank.R

/**
 * Created by Evhenyi Shcherbyna on 04.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ArcTrait private constructor(private val view: View) {

    companion object {
        fun newInstance(view: ArcView): ArcTrait {
            return ArcTrait(view as View)
        }

        fun findArcView(view: View?): View? {
            return when (view) {
                is ArcView -> view
                is ViewGroup -> {
                    for (i in 0 until view.childCount) {
                        val child = view.getChildAt(i)
                        if (child is ArcView) return child
                    }
                    null
                }
                else -> null
            }
        }
    }

    val arcHeight = view.resources.getDimensionPixelOffset(R.dimen.arc_height)
    private val colorAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)

    private var fillPaint = Paint()
    private var shapePath = Path()
    private val clipPaint = Paint()
    private val clipPath = Path()

    private var startColors = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT)
    private var endColors = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT)
    private var currentColors = intArrayOf(Color.TRANSPARENT, Color.TRANSPARENT)

    init {
        view.apply {
            transitionName = ArcView.TRANSITION_NAME
            setBackgroundColor(Color.TRANSPARENT)
            stateListAnimator = null
        }

        clipPaint.apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.FILL
        }

        fillPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val argbEvaluator = ArgbEvaluator()
        colorAnimator.apply {
            duration = ArcView.GRADIENT_DURATION
            addUpdateListener {
                val startColor = argbEvaluator.evaluate(it.animatedFraction, startColors[0], endColors[0]) as Int
                val endColor = argbEvaluator.evaluate(it.animatedFraction, startColors[1], endColors[1]) as Int
                currentColors[0] = startColor
                currentColors[1] = endColor
                initShader()
                view.invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    startColors[0] = endColors[0]
                    startColors[1] = endColors[1]
                }
            })
        }
    }

    private fun initShader() {
        val gradient = LinearGradient(
                0f, (view.measuredHeight / 2).toFloat(), view.measuredWidth.toFloat(), (view.measuredHeight / 2).toFloat(),
                currentColors[0], currentColors[1],
                Shader.TileMode.CLAMP
        )
        fillPaint.shader = gradient
    }

    fun setColors(colors: IntArray, animate: Boolean = false) {
        if (animate) {
            this.endColors = colors
            colorAnimator.start()
        } else {
            this.startColors = colors
            this.currentColors = colors
            initShader()
            view.invalidate()
        }
    }

    fun getColors(): IntArray = currentColors

    fun dispatchDraw(canvas: Canvas) {
        canvas.drawPath(shapePath, fillPaint)
        canvas.clipPath(clipPath)
    }

    fun onSizeChanged(w: Int, h: Int) {
        initShader()

        shapePath.reset()
        shapePath.moveTo(0f, 0f)
        shapePath.lineTo(0f, h.toFloat())
        shapePath.quadTo(w.shr(1).toFloat(), (h - arcHeight * 2).toFloat(), w.toFloat(), h.toFloat())
        shapePath.lineTo(w.toFloat(), 0f)
        shapePath.close()

        clipPath.reset()
        clipPath.moveTo(0f, 0f)
        clipPath.lineTo(0f, h.toFloat())
        clipPath.quadTo(w.shr(1).toFloat(), h.toFloat() - arcHeight * 2, w.toFloat(), h.toFloat())
        clipPath.lineTo(w.toFloat(), 0f)
        clipPath.close()
    }

    interface HasArcView {
        fun getArcView(): View?
    }

}