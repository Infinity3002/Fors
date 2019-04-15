package proglife.fora.bank.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import proglife.fora.bank.R

/**
 * Created by Evhenyi Shcherbyna on 08.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CollapsedLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val paint = Paint()
    val toolbarSize = context.resources.getDimension(R.dimen._70sdp)
    val arcHeight = context.resources.getDimensionPixelOffset(R.dimen._20sdp)
    val path = Path()

    init {
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
    }

    var factor: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    override fun dispatchDraw(canvas: Canvas) {
        val tHeight = height - toolbarSize + arcHeight
        val offset = tHeight - tHeight * factor

        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(0f, offset + toolbarSize)
        path.quadTo((width / 2).toFloat(), offset + toolbarSize - arcHeight * 2, width.toFloat(), offset + toolbarSize)
        path.lineTo(width.toFloat(), 0f)
        path.close()

        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.drawARGB((255 * factor).toInt(), 194, 58, 81)
    }

}