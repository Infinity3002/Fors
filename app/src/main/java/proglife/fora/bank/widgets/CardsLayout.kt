package proglife.fora.bank.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.support.v4.view.GestureDetectorCompat
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import proglife.fora.bank.R
import kotlin.math.absoluteValue


/**
 * Created by Evhenyi Shcherbyna on 12.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CardsLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        private const val LIMIT = 4
    }

    private val overSpace = context.resources.getDimensionPixelOffset(R.dimen._5sdp)
    private val space = context.resources.getDimensionPixelOffset(R.dimen._34sdp)
    private var yDelta: Int = 0
    private var places = IntArray(0)
    private var views = mutableListOf<View>()

    var callback : Callback? = null

    fun init() {
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i)
            views.add(child)
            var blockedTouch = false
            val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent?) {
                    if (selectedView != null) return
                    blockedTouch = true
                    callback?.onLongPress(child)
                }

                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    if (selectedView == null) callback?.onClick(child)
                    return true
                }
            })
            var startYPosition = 0f
            var wasMove = false
            child.setOnTouchListener { v, event ->
                if (gestureDetector.onTouchEvent(event)) return@setOnTouchListener true
                if (!canDrag(v)) return@setOnTouchListener true
                val y = event.rawY
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        blockedTouch = false
                        yDelta = (y - v.y).toInt()
                        startYPosition = v.y
                        wasMove = false
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (!blockedTouch) {
                            val moveTo = y - yDelta
                            val newYPosition = when {
                                moveTo < paddingTop && selectedView == null -> paddingTop.toFloat()
                                moveTo < 0f -> 0f
                                moveTo > measuredHeight - v.measuredHeight -> measuredHeight.toFloat() - v.measuredHeight
                                else -> moveTo
                            }
                            if ((newYPosition - startYPosition).absoluteValue > 1) {
                                v.y = newYPosition
                                wasMove = true
                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        if (wasMove && !blockedTouch) bringToBack(v)
                    }
                }
                true
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val availableWidth = measuredWidth - paddingRight - paddingLeft
        val availableHeight = measuredHeight - paddingTop - paddingBottom

        if (places.size != childCount) {
            places = IntArray(childCount)
            init()
        }

        val stackSize = childCount - LIMIT

        for (i in childCount - 1 downTo 0) {
            val index = if (i - stackSize < 0) 0 else i - stackSize
            val offset = if (i - stackSize < 0) overSpace else 0
            val child = getChildAt(i)

            val cLeft = 0
            val cRight = availableWidth
            val cTop = space * index + paddingTop - offset
            val cBottom = space * index + child.measuredHeight + paddingTop - offset

            places[i] = cTop

            child.layout(cLeft, cTop, cRight, cBottom)
        }
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }
    }

    private fun canDrag(view: View) : Boolean {
        return (selectedView == view) || (selectedView == null && views.first() == view)
    }

    private fun bringToBack(view: View) {
        if (selectedView != null) {
            selectedView = null
            callback?.onExtend()
        } else {
            views.remove(view)
            views.add(view)
            callback?.onMoveToBack(view.tag)
        }

        refreshZIndexes()
        refreshLocations()
    }

    private fun refreshZIndexes() {
        for (i in 0 until views.size) {
            val view = views[i]
            view.z = (views.size - i).toFloat()
        }
    }

    private fun refreshLocations() {
        views.reversed().forEachIndexed { index, view ->
            view.animate()
                    .y(places[index].toFloat())
                    .rotation(0f)
                    .setDuration(300).start()
        }
    }

    var selectedView : View? = null

    fun collapse(view: View) {
        callback?.onCollapse(view)
        selectedView = view
        var last = 0f
        views.filter { it != view }.forEachIndexed { i, v ->
            var transYBy = - v.y - v.measuredHeight * 0.9f + v.measuredHeight * 0.05f * i
            if (i >= LIMIT) {
                transYBy = last
            } else {
                last = transYBy
            }
            v.animate()
                    .translationYBy(transYBy)
                    .setDuration(500)
                    .start()
        }
        view.animate()
                .translationYBy(measuredHeight.shr(1).toFloat() - view.y - view.measuredHeight.shr(1))
                .translationZ(100f)
                .setDuration(500)
                .start()
    }

    interface Callback {
        fun onLongPress(view: View)
        fun onClick(view: View)
        fun onCollapse(view: View)
        fun onExtend()
        fun onMoveToBack(data: Any?)
    }

    override fun onSaveInstanceState(): Parcelable {
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
    }
}