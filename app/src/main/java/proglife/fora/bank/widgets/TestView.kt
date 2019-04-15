package proglife.fora.bank.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import proglife.fora.bank.R

/**
 * Created by Evhenyi Shcherbyna on 05.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class TestView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    private val RADIUS = 100f
    private val paint = Paint()
    private val textPaint = Paint()
    private val path = Path()

    init {
        paint.color = Color.RED
        textPaint.color = Color.GREEN
        textPaint.textSize = context.resources.getDimensionPixelOffset(R.dimen._13sdp).toFloat()
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        val x = measuredWidth.shr(1).toFloat()
        val y = measuredHeight.shr(1).toFloat()
        canvas.drawCircle(x, y, RADIUS, paint)

        path.reset()
        path.addCircle(x, y, RADIUS, Path.Direction.CW)

        val text = "Настройки"
        val bounds = Rect()
        paint.getTextBounds(
                text,
                0,
                text.length,
                bounds
        )

        val offset = ( 0.5f * Math.PI * RADIUS).toFloat()
        canvas.drawTextOnPath(text, path,
                offset, 0f, textPaint)
    }

}