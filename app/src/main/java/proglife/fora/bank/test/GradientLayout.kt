package proglife.fora.bank.test

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.annotation.FloatRange
import android.util.AttributeSet
import android.view.View
import proglife.fora.bank.R


/**
 * Created by Evhenyi Shcherbyna on 07.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@SuppressLint("Recycle")
class GradientLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var colors = intArrayOf(Color.WHITE, Color.BLACK)
    private var gradientOffset: Float = 0f
    private var shapePath = Path()
    private var tempPath = Path()
    private var fillPaint = Paint()
    private var percent = 0f


    init {
        fillPaint.color = colors[0]
        fillPaint.style = Paint.Style.FILL
        fillPaint.isAntiAlias = true

        val attributes = getContext().theme.obtainStyledAttributes(attrs, R.styleable.GradientLayout, defStyleAttr, 0)
        try {
            val statesColorsResourceId = attributes.getResourceId(R.styleable.GradientLayout_colors, 0)
            val colorsResources = attributes.resources.getStringArray(statesColorsResourceId)
            val colorsInts = IntArray(colorsResources.size)
            for (i in 0 until colorsResources.size) {
                colorsInts[i] = Color.parseColor(colorsResources[i])
            }
            setColors(colorsInts)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            attributes.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(-gradientOffset, 0f)
        shapePath.offset(gradientOffset, 0f, tempPath)
        canvas.drawPath(tempPath, fillPaint)
        canvas.restore()

        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        initShader()

        shapePath.reset()
        shapePath.moveTo(0f, 0f)
        shapePath.lineTo(w.toFloat(), 0f)
        shapePath.lineTo(w.toFloat(), h.toFloat())
        shapePath.lineTo(0f, h.toFloat())
        shapePath.close()
    }

    private fun initShader() {
        val gradient = LinearGradient(
                0f, (height / 2).toFloat(), (width * (colors.size - 1)).toFloat(), (height / 2).toFloat(),
                colors, null, Shader.TileMode.CLAMP
        )
        fillPaint.shader = gradient
    }

    fun setColors(colors : IntArray) {
        this.colors = colors
        initShader()
    }

    fun setOffsetPercentage(@FloatRange(from = 0.0, to = 1.0) percent : Float) {
        val colorsCount = this.colors.size - 1
        val gradientWidth = measuredWidth * colorsCount
        this.percent = percent
        gradientOffset = gradientWidth * percent
        invalidate()
    }

}