package proglife.fora.bank.widgets.arc

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import proglife.fora.bank.R

/**
 * Created by Evhenyi Shcherbyna on 04.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ArcBackgroundView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), ArcView {

    private val mArcTrait = ArcTrait.newInstance(this)

    init {
        val attributes = getContext().theme.obtainStyledAttributes(attrs, R.styleable.ArcBackgroundView, defStyleAttr, 0)
        try {
            val statesColorsResourceId = attributes.getResourceId(R.styleable.ArcBackgroundView_colors, 0)
            val colorsResources = attributes.resources.getStringArray(statesColorsResourceId)
            setColors(intArrayOf(Color.parseColor(colorsResources[0]), Color.parseColor(colorsResources[1])))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            attributes.recycle()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        getChildAt(0)?.layout(l, t, r, b - mArcTrait.arcHeight)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height + mArcTrait.arcHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
        measureChild(getChildAt(0), widthMeasureSpec, heightMeasureSpec)
    }

    override fun setColors(colors: IntArray, animate: Boolean) {
        mArcTrait.setColors(colors, animate)
    }

    override fun getColors(): IntArray {
        return mArcTrait.getColors()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mArcTrait.onSizeChanged(w, h)
    }

    override fun dispatchDraw(canvas: Canvas) {
        mArcTrait.dispatchDraw(canvas)
        super.dispatchDraw(canvas)
    }

}