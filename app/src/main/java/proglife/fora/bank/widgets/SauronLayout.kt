package proglife.fora.bank.widgets

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import proglife.fora.bank.R

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class SauronLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val arcHeight = context.resources.getDimensionPixelOffset(R.dimen.arc_height)
    private var clipPath: Path? = null

    private fun createClipPath(): Path {
        val path = Path()

        path.moveTo(0f, arcHeight.toFloat())
        path.quadTo((width / 2).toFloat(), 0f, width.toFloat(), arcHeight.toFloat())
        path.lineTo(width.toFloat(), (height - arcHeight).toFloat())
        path.quadTo((width / 2).toFloat(), height.toFloat(), 0f, height - arcHeight.toFloat())
        path.close()

        return path
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            clipPath = createClipPath()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        super.dispatchDraw(canvas)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
        canvas.drawPath(clipPath, paint)
        canvas.restoreToCount(saveCount)
        paint.xfermode = null
    }

}