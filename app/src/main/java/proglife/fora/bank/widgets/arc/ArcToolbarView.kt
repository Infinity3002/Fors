package proglife.fora.bank.widgets.arc

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.design.widget.AppBarLayout
import android.util.AttributeSet
import proglife.fora.bank.R

/**
 * Created by Evhenyi Shcherbyna on 19.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@SuppressLint("Recycle")
class ArcToolbarView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppBarLayout(context, attrs), ArcView {

    private var isFullScreen = false

    private val mArcTrait = ArcTrait.newInstance(this)

    init {
        val attributes = getContext().theme.obtainStyledAttributes(attrs, R.styleable.ArcToolbarView, defStyleAttr, 0)
        try {
            isFullScreen = attributes.getBoolean(R.styleable.ArcToolbarView_fullScreen, false)
            val statesColorsResourceId = attributes.getResourceId(R.styleable.ArcToolbarView_colors, 0)
            val colorsResources = attributes.resources.getStringArray(statesColorsResourceId)
            setColors(intArrayOf(Color.parseColor(colorsResources[0]), Color.parseColor(colorsResources[1])))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            attributes.recycle()
        }
    }

//    var offset: Float = 0f
//        set(value) {
//            field = value
//            invalidate()
//        }

    override fun dispatchDraw(canvas: Canvas) {
        mArcTrait.dispatchDraw(canvas)
        super.dispatchDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isFullScreen) {
            val height = MeasureSpec.getSize(heightMeasureSpec)
            val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height + mArcTrait.arcHeight + totalScrollRange, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mArcTrait.onSizeChanged(w, h)
    }

    override fun getColors(): IntArray = mArcTrait.getColors()

    override fun setColors(colors: IntArray, animate: Boolean) {
        mArcTrait.setColors(colors, animate)
    }

}