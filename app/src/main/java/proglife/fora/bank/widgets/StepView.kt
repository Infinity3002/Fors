package proglife.fora.bank.widgets

import android.app.Fragment
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import proglife.fora.bank.R
import android.os.Parcel




class StepView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    //количество отрисовок
    private val countDraw = 10f

    //количество точек
    private val step = 4
    private var radiusDefualt = context.resources.getDimensionPixelSize(R.dimen._4sdp).toFloat()
    private val space = context.resources.getDimensionPixelSize(R.dimen._6sdp)
    private val heightMax = (radiusDefualt * 2).toInt()
    private val widthMax = (radiusDefualt * 2).toInt() * 6 + space * 6

    private val currentPoint = (radiusDefualt * 2) + space
    private val twoPoint = ((radiusDefualt * 0.9f) * 2) + space
    private val triPoint = ((radiusDefualt * 0.7f) * 2) + space

    private var drawPaint: Paint = Paint()
    private var currentPaint: Paint = Paint()

    private var isAnimate = false
    private var animateOffset = currentPoint
    private var stepAnimate = countDraw


    private var currentSelect = 1

    init {
        currentPaint.color = Color.parseColor("#EA4442")
        currentPaint.isAntiAlias = true
        drawPaint.color = Color.parseColor("#DCDCDC")
        drawPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            layoutParams.height = heightMax
            layoutParams.width = widthMax
        setMeasuredDimension(layoutParams.width, layoutParams.height)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val yCenter = height / 2f
        var xCenter = width / 2f

        if (isAnimate) {
            xCenter += animateOffset / (countDraw - stepAnimate)
            stepAnimate--
        }

        canvas.drawCircle(xCenter, yCenter, radiusDefualt, currentPaint)
        for (i in 1 until step + 1) {
            var x = 0f
            var radius = 0f
            var shift = 0f

            val point = i - currentSelect
            when (point) {
                -1 -> {
                    x = xCenter - currentPoint
                    radius = radiusDefualt * 0.9f
                    shift = 0f
                }
                1 -> {
                    x = xCenter + currentPoint
                    radius = radiusDefualt * 0.9f
                    shift = 0f
                }
                2 -> {
                    x = xCenter + (twoPoint + currentPoint)
                    radius = radiusDefualt * 0.7f
                    shift = yCenter * 0.009f
                }
                -2 -> {
                    x = xCenter - (twoPoint + currentPoint)
                    radius = radiusDefualt * 0.7f
                    shift = yCenter * 0.009f
                }
                -3 -> {
                    //лево крайний поинт
                    x = xCenter - (triPoint + currentPoint + twoPoint)
                    radius = radiusDefualt * 0.5f
                    shift = yCenter * 0.25f
                }
                3 -> {
                    //право крайний поинт
                    x = xCenter + (triPoint + currentPoint + twoPoint)
                    radius = radiusDefualt * 0.5f
                    shift = yCenter * 0.25f
                }
            }

            canvas.drawCircle(x, yCenter + shift, radius, drawPaint)
            if (isAnimate) {
                if (stepAnimate <= 0) {
                    isAnimate = false
                }

                invalidate()
            }
        }
    }

    fun selectPage(page: Int, animate: Boolean = true) {
        if (page > step || page == 0) return
        animateOffset = if (page > currentSelect) radiusDefualt else radiusDefualt.unaryMinus()
        stepAnimate = countDraw
        currentSelect = page
        isAnimate = animate

        invalidate()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
    }

    override fun onSaveInstanceState(): Parcelable {
      //  val savedState  = super.onSaveInstanceState() as SavedState
       // savedState.stateToSave = currentSelect
        return super.onSaveInstanceState()
    }

    internal class SavedState : View.BaseSavedState {
        var stateToSave: Int = 0

        constructor(superState: Parcelable) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            this.stateToSave = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(this.stateToSave)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }


}