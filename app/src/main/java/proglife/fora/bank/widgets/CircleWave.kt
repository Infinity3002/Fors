package proglife.fora.bank.widgets

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import proglife.fora.bank.R

class CircleWave @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    val paint = Paint()
    var blocked = false

    val animationRunnable: Runnable = Runnable {
        startAnimation()
    }

    lateinit var animation: ValueAnimator

    init {
        paint.color = Color.WHITE
        paint.strokeWidth = context.resources.getDimensionPixelSize(R.dimen._1sdp).toFloat()
        paint.style = Paint.Style.STROKE
    }

    var factor: Float = 1.5f
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        alpha = factor
        canvas.drawCircle(width / 2f, height / 2f, height * factor, paint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animation.cancel()
        removeCallbacks(animationRunnable)
    }

    fun startAnimation(){
        animation = ValueAnimator.ofFloat(0.2f,1.5f)
        animation.duration = 500
        animation.addUpdateListener { animation -> factor = animation.animatedValue as Float }
        animation.addListener(object : Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                if (!blocked) postDelayed(animationRunnable, 1600)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
        animation.repeatCount = 2
        animation.start()
    }

}