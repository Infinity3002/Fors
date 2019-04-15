package proglife.fora.bank.widgets.arctabs

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.View

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class GradientBackgroundView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    init {
        val size = Point()
        (context as Activity).windowManager.defaultDisplay.getSize(size)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) * 3, MeasureSpec.EXACTLY)
        val h = resolveSizeAndState(MeasureSpec.getSize(heightMeasureSpec), heightMeasureSpec, 0)
        setMeasuredDimension(w, h)
    }

}