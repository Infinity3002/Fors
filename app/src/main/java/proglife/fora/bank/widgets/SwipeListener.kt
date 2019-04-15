package proglife.fora.bank.widgets

import android.util.Log
import android.view.MotionEvent
import android.view.View


class SwipeListener(val view: View) : View.OnTouchListener {
    private var downX: Float = 0.toFloat()
    private var upX: Float = 0.toFloat()
    private var upY: Float = 0.toFloat()
    private var translationX: Float = view.width.toFloat() + 10f
    private var state: State = State.HIDE

    override fun onTouch(v: View, event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                upX = event.x

                val deltaX = downX - upX

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    view.visibility = View.VISIBLE
                    if (deltaX < 0) {
                        if (state == State.HIDE)
                            return false
                        translationX = deltaX.unaryMinus() - MIN_DISTANCE

                        if(translationX > view.width.toFloat()+ 10f) {
                            translationX = view.width.toFloat()+ 10f
                        }
                        view.translationX = translationX

                        return true
                    }
                    if (deltaX > 0) {
                        if (state == State.SHOW)
                            return false
                        translationX = (view.width.toFloat()+ 10f) - (deltaX - MIN_DISTANCE)
                        if (translationX < 0) {
                            translationX = 0f
                        }
                        view.translationX = translationX
                        return true
                    }
                }


            }
            else -> {
                upX = event.x
                val deltaX = downX - upX
                if(Math.abs(deltaX) > MIN_DISTANCE) {
                    if (deltaX < 0) {
                        state = State.HIDE
                        view.animate()
                                .translationX(view.width.toFloat()+ 10f)
                                .setListener(null)
                    } else {
                        state = State.SHOW
                        view.animate()
                                .translationX(0f)
                                .setListener(null)
                    }
                    return true
                }
            }
        }
        return false
    }

    enum class State {
        SHOW,
        HIDE,
        NONE
    }

    companion object {

        private val logTag = "Swipe"
        private val MIN_DISTANCE = 60
    }
}