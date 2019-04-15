package proglife.fora.bank.widgets.arctabs

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import proglife.fora.bank.R

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@SuppressLint("Recycle")
class CurvedTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    private var paint = Paint()
    private var text: String = ""
    private val bounds = Rect()
    private val path = Path()
    private var textColor: Int = Color.parseColor("#BBFFFFFF")
    private val radius: Int = context.resources.getDimensionPixelOffset(R.dimen._380sdp) * 2
//    private val bonus: Int = context.resources.getDimensionPixelOffset(R.dimen._5sdp)

    init {
        val attributes = getContext().theme.obtainStyledAttributes(attrs, R.styleable.CurvedTextView, defStyleAttr, 0)
        try {
            text = attributes.getString(R.styleable.CurvedTextView_text) ?: ""
            textColor = attributes.getColor(R.styleable.CurvedTextView_textColor, Color.parseColor("#BBFFFFFF"))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            attributes.recycle()
        }

        val size = Point()
        (context as Activity).windowManager.defaultDisplay.getSize(size)
        paint.style = Paint.Style.FILL
        paint.color = textColor
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = resources.getDimensionPixelOffset(R.dimen._13sdp).toFloat()

        paint.getTextBounds(
                text,
                0,
                text.length,
                bounds
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawTextOnPath(text, path, (0.5f * Math.PI * radius).toFloat(), 0f, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeW = paddingLeft + bounds.width() * 1.15f + paddingRight
        val sizeH = paddingTop + paint.textSize * 2 + paddingBottom
        val w = resolveSizeAndState(sizeW.toInt(), widthMeasureSpec, 0)
        val h = resolveSizeAndState(sizeH.toInt(), heightMeasureSpec, 0)
        path.reset()
        path.addCircle(w.shr(1).toFloat(), radius + sizeH / 2, radius.toFloat(), Path.Direction.CW)
        setMeasuredDimension(w, h)
    }

    fun setText(text: String) {
        this.text = text
        paint.getTextBounds(
                text,
                0,
                text.length,
                bounds
        )
        requestLayout()
    }

    fun setColor(color: Int, typeface: String = "sans-serif-light") {
        this.paint.color = color
        this.paint.typeface = Typeface.create(typeface, Typeface.NORMAL)
        invalidate()
    }

}