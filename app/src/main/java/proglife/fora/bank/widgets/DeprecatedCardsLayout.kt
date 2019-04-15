package proglife.fora.bank.widgets

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import proglife.fora.bank.R
import kotlin.math.absoluteValue


/**
 * Created by Evhenyi Shcherbyna on 12.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class DeprecatedCardsLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val space = context.resources.getDimensionPixelOffset(R.dimen._20sdp)
    private val frontOffset = context.resources.getDimensionPixelOffset(R.dimen._10sdp)

    private var yDelta: Int = 0
    private var places = IntArray(0)

    private var views = mutableListOf<View>()
    private var viewsStates = mutableMapOf<View, Boolean>()

    private val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            println("Scroll $distanceY")
            moveViews(distanceY)
            return true
        }
        override fun onLongPress(e: MotionEvent?) {
            Toast.makeText(context, "LongPress", Toast.LENGTH_SHORT).show()
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
            return super.onSingleTapUp(e)
        }
    })

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        places = IntArray(childCount)

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            views.add(child)
            viewsStates[child] = false
        }
//            val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
//                override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
//                    child.y = distanceY
//                    println("Scroll $distanceY")
//                    moveViews(distanceY)
//                    return true
//                }
//                override fun onLongPress(e: MotionEvent?) {
//                    println("LongPress")
//                }
//
//                override fun onSingleTapUp(e: MotionEvent?): Boolean {
//                    println("onSingleTapUp")
//                    return super.onSingleTapUp(e)
//                }
//
//                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
//                    println("onSingleTapConfirmed")
//                    return super.onSingleTapConfirmed(e)
//                }
//            })
//            child.setOnTouchListener { v, event ->
//                println("Gesture ${gestureDetector.onTouchEvent(event)}")
//                if (!canDrag(v)) return@setOnTouchListener false
//                val y = event.rawY
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        yDelta = (y - v.y).toInt()
//                    }
//                    MotionEvent.ACTION_MOVE -> {
//                        val moveTo = y - yDelta
//                        v.y = when {
//                            moveTo < 0 -> 0f
//                            moveTo > measuredHeight - v.measuredHeight -> measuredHeight.toFloat() - v.measuredHeight
//                            else -> moveTo
//                        }
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        bringToBack(v)
//                    }
//                }
//                true
//            }
//        }
//        views.reverse()
    }

    var maxY = 0f

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val availableWidth = measuredWidth - paddingRight - paddingLeft
        val availableHeight = measuredHeight - paddingTop - paddingBottom

        for (i in 0 until childCount) {
            val child = getChildAt(i)

            val cLeft = 0
            val cRight = availableWidth
            val cTop = space * i + paddingTop
            val cBottom = space * i + child.measuredHeight + paddingTop

            places[i] = cTop

            child.layout(cLeft, cTop, cRight, cBottom)

            maxY = child.y
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
        return views.first() == view
    }

    private fun bringToBack(view: View) {
        views.remove(view)
        views.add(view)
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
            view.animate().y(places[index].toFloat()).setDuration(300).start()
        }
    }

    private fun moveViews(offset: Float) {
        if (offset > 0) return
        views.toList().forEachIndexed { index, view ->
            val res = computePositionY(view, -offset)
            view.y = res
        }
        refreshZIndexes()
    }

    private fun computePositionY(view: View, offset: Float) : Float {
        val limitedOffset = if (offset.absoluteValue > space) space.toFloat() else offset
        println("Offset $limitedOffset")
        return if (viewsStates[view]!!) {
//            val newY = view.y - limitedOffset / space * maxY
            val newY = view.y - limitedOffset // 130 - 70 = 60

            if (newY < 0) {
                viewsStates[view] = false
                newY + view.y // -60 + 100 = 40
            } else {
                newY // 60
            }
        } else {
            val newY = view.y + limitedOffset // 130 + 70 = 200
            if (newY > maxY) { // 200 > 150
                viewsStates[view] = true
                views.remove(view)
                views.add(view)
//                view.y - (newY - maxY) / space * maxY
                maxY - (newY - maxY) // 150 - (130 - (200 - 150)) =
            } else {
                newY // 200
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        when (event?.action) {
            MotionEvent.ACTION_UP -> refreshLocations()
        }
        return true
    }

//    private fun computePositionY(view: View, offset: Float) : Pair<Float, Boolean> {
//        val newY = view.y + offset
//        return if (newY > maxY) Pair(newY - maxY, true) else Pair(newY, false)
//    }

}