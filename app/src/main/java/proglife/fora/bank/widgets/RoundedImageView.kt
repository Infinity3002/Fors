package proglife.fora.bank.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.graphics.RectF
import android.graphics.drawable.Drawable
import proglife.fora.bank.R


/**
 * Created by Evhenyi Shcherbyna on 18.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class RoundedImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val mClipPath = Path()
    private var mRadius: Float = -1f
    private var foregroundDrawable: Drawable? = null
        set(value) {
            if (field == value) {
                return
            }
            if (field != null) {
                field?.callback = null
                unscheduleDrawable(foregroundDrawable)
            }

            field = value

            if (value != null) {
                value.callback = this
                if (value.isStateful) {
                    value.state = drawableState
                }
            }
            requestLayout()
            invalidate()
        }

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.RoundedImageView, defStyleAttr, 0)

            mRadius = typedArray.getDimension(R.styleable.RoundedImageView_cornerRadius, -1f)
            foregroundDrawable = typedArray.getDrawable(R.styleable.RoundedImageView_foreground)

            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(mClipPath)
        super.onDraw(canvas)
        foregroundDrawable?.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val radius = if (mRadius <= 0f) Math.min(w, h).shr(1).toFloat() else mRadius
        mClipPath.reset()
        val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
        mClipPath.addRoundRect(rect, radius, radius, Path.Direction.CW)

        if (foregroundDrawable != null) {
            foregroundDrawable?.setBounds(0, 0, w, h)
            invalidate()
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who == foregroundDrawable
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        if (foregroundDrawable != null) foregroundDrawable?.jumpToCurrentState()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (foregroundDrawable != null && foregroundDrawable?.isStateful == true) {
            foregroundDrawable?.state = drawableState
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (foregroundDrawable != null) {
            foregroundDrawable?.setBounds(0, 0, measuredWidth, measuredHeight)
            invalidate()
        }
    }

}