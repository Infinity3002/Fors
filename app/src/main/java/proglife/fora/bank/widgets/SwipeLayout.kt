package proglife.fora.bank.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class SwipeLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        const val SIDE_LEFT = 0
        const val SIDE_RIGHT = 1
        const val SIDE_CENTER = 2

        private val logTag = "Swipe"
        private val MIN_DISTANCE = 60
    }

    private var downX: Float = 0.toFloat()
    private var upX: Float = 0.toFloat()

    private var isFlingCenterView = false
    private var stateRight: State = State.HIDE

    private var stateLeft: State = State.HIDE

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val availableWidth = measuredWidth

        val contentChild: View? = getChildAt(0)
        contentChild?.layout(0, 0, availableWidth, contentChild.measuredHeight)

        val firstChild: View? = getChildAt(1)
        firstChild?.let {
            firstChild.layout(measuredWidth, 0, measuredWidth + it.measuredWidth, it.measuredHeight)
        }

        val secondChild: View? = getChildAt(2)
        secondChild?.let {
            secondChild.layout(-it.measuredWidth, 0, 0, it.measuredHeight)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (i == 0) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                height = child.measuredHeight
            } else {
                measureChild(child, widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
            }
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))

    }

    fun getSize(side: Int): Int {
        return when (side) {
            SIDE_RIGHT -> getChildAt(1)?.measuredWidth ?: 0
            SIDE_LEFT -> getChildAt(2)?.measuredWidth ?: 0
            else -> 0
        }
    }

    fun getTransations(side: Int): Float {
        return when (side) {
            SIDE_CENTER -> getChildAt(0)?.translationX ?: 0f
            SIDE_RIGHT -> getChildAt(1)?.translationX ?: 0f
            SIDE_LEFT -> getChildAt(2)?.translationX ?: 0f
            else -> 0f
        }
    }


    fun setTranslation(side: Int, translation: Int) {
        val view: View? = when (side) {
            SIDE_CENTER -> getChildAt(0)
            SIDE_RIGHT -> getChildAt(1)
            SIDE_LEFT -> getChildAt(2)
            else -> null
        }

        view?.let { it.translationX = translation.toFloat() }
    }

    fun animate(side: Int, translation: Int): Boolean {
        val view: View? = when (side) {
            SIDE_CENTER -> getChildAt(0)
            SIDE_RIGHT -> getChildAt(1)
            SIDE_LEFT -> getChildAt(2)
            else -> null
        }

        view?.animate()?.translationX(translation.toFloat())?.setListener(null)
        return view != null
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                upX = event.x
                Log.d("LOGS", "event.x: " + event.x)
                val deltaX = downX - upX

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    if (deltaX < 0) {
                        if (getTransations(SIDE_RIGHT) < 0) {
                            if (getTransations(SIDE_RIGHT) == 0f)
                                return super.onTouchEvent(event)
                            var translationXRight = (getSize(SIDE_RIGHT) - (deltaX.unaryMinus() - MIN_DISTANCE)).unaryMinus().toInt()

                            if (translationXRight > 0) {
                                translationXRight = 0
                            }

                            setTranslation(SIDE_RIGHT, translationXRight)
                            setTranslation(SIDE_CENTER, translationXRight)
                        } else {
                            if (getTransations(SIDE_LEFT) == getSize(SIDE_LEFT).toFloat())
                                return super.onTouchEvent(event)
                            var translationXLeft = (deltaX + MIN_DISTANCE).unaryMinus().toInt()

                            if (translationXLeft > getSize(SIDE_LEFT)) {
                                translationXLeft = getSize(SIDE_LEFT)
                            }
                            setTranslation(SIDE_LEFT, translationXLeft)
                            setTranslation(SIDE_CENTER, translationXLeft)

                        }

                        return true
                    } else if (deltaX > 0) {
                        if (getTransations(SIDE_LEFT) > 0f) {
                            if (getTransations(SIDE_LEFT) == 0f)
                                return super.onTouchEvent(event)
                            var translationXLeft = getSize(SIDE_LEFT) + (deltaX.unaryMinus() + MIN_DISTANCE).toInt()
                            if (translationXLeft < 0) {
                                translationXLeft = 0
                            }
                            setTranslation(SIDE_LEFT, translationXLeft)
                            setTranslation(SIDE_CENTER, translationXLeft)

                        } else {
                            if (getTransations(SIDE_RIGHT) == getSize(SIDE_RIGHT).unaryMinus().toFloat())
                                return super.onTouchEvent(event)
                            var translationXRight = (deltaX - MIN_DISTANCE).unaryMinus().toInt()
                            if (translationXRight < getSize(SIDE_RIGHT).unaryMinus()) {
                                translationXRight = getSize(SIDE_RIGHT).unaryMinus()
                            }
                            setTranslation(SIDE_RIGHT, translationXRight)
                            setTranslation(SIDE_CENTER, translationXRight)
                        }
                        return true
                    }
                    return super.onTouchEvent(event)
                }
            }
            else -> {
                upX = event.x
                val deltaX = downX - upX
                if (Math.abs(deltaX) > MIN_DISTANCE
                        || (getTransations(SIDE_LEFT) > 0f && getTransations(SIDE_LEFT) != getSize(SIDE_LEFT).toFloat())
                        || (getTransations(SIDE_RIGHT) < getSize(SIDE_RIGHT).toFloat() && getTransations(SIDE_RIGHT) != getSize(SIDE_RIGHT).unaryMinus().toFloat())) {
                    if (deltaX < 0) {
                        if (getTransations(SIDE_LEFT) == 0f && getTransations(SIDE_RIGHT) < 0f) {
                            if (animate(SIDE_RIGHT, 0)) {
                                animate(SIDE_CENTER, 0)
                                stateRight = State.HIDE
                            }
                        } else
                            if (getTransations(SIDE_RIGHT) == 0f && getTransations(SIDE_LEFT) > 0) {
                                if (animate(SIDE_LEFT, getSize(SIDE_LEFT))) {
                                    stateLeft = State.SHOW
                                    animate(SIDE_CENTER, getSize(SIDE_LEFT))
                                }
                            }
                    } else {
                        if (getTransations(SIDE_LEFT) == 0f && getTransations(SIDE_RIGHT) < 0) {
                            if (animate(SIDE_RIGHT, getSize(SIDE_RIGHT).unaryMinus())) {
                                animate(SIDE_CENTER, getSize(SIDE_RIGHT).unaryMinus())
                                stateRight = State.SHOW
                            }
                        } else
                            if (getTransations(SIDE_RIGHT) == 0f && getTransations(SIDE_LEFT) < getSize(SIDE_LEFT)) {
                                if (animate(SIDE_LEFT, 0)) {
                                    stateLeft = State.HIDE
                                    animate(SIDE_CENTER, 0)
                                }
                            }
                    }
                    return super.onTouchEvent(event)
                }

                /*    if (deltaX < 0) {
                        if (stateRight == State.SHOW) {
                            animate(SIDE_CENTER, 0)
                            if (animate(SIDE_RIGHT, getSize(SIDE_RIGHT)))
                                stateRight = State.HIDE
                        } else {
                            if (animate(SIDE_LEFT, getSize(SIDE_LEFT))){
                                stateLeft = State.SHOW
                                animate(SIDE_CENTER, getSize(SIDE_LEFT))
                            }

                        }
                    } else {
                        if (stateLeft == State.SHOW) {
                            animate(SIDE_CENTER, 0)
                            if (animate(SIDE_LEFT, 0))
                                stateLeft = State.HIDE
                        } else {
                            animate(SIDE_CENTER, getSize(SIDE_RIGHT).unaryMinus())
                            if (animate(SIDE_RIGHT, getSize(SIDE_RIGHT).unaryMinus()))
                                stateRight = State.SHOW
                        }
                    }*/
                return super.onTouchEvent(event)

            }
        }
        return true
    }

    enum class State {
        SHOW,
        HIDE,
        NONE
    }

}