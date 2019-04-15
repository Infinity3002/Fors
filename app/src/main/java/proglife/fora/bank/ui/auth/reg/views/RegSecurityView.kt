package proglife.fora.bank.ui.auth.reg.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpDelegate
import kotlinx.android.synthetic.main.fragment_auth_reg_security.view.*
import proglife.fora.bank.R
import proglife.fora.bank.utils.AnimationDirection
import proglife.fora.bank.utils.FingerprintUtil
import proglife.fora.bank.utils.inflate

/**
 * Created by Evhenyi Shcherbyna on 11.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class RegSecurityView(context: Context, attributeSet: AttributeSet) :
        FrameLayout(context, attributeSet) {

    companion object {
        const val TOUCH_ID_CLICK = 0
        const val NEXT_CLICK = 1
    }

    private var mCallback: ((Int) -> Unit)? = null
    private lateinit var mParentDelegate: MvpDelegate<*>
    private val mMvpDelegate: MvpDelegate<RegSecurityView> by lazy {
        val value = MvpDelegate(this)
        value.setParentDelegate(mParentDelegate, id.toString())
        value
    }

    init {
        inflate(R.layout.fragment_auth_reg_security, true)
    }

    fun init(parentDelegate: MvpDelegate<*>, callback: (Int) -> Unit): RegSecurityView {
        mCallback = callback
        mParentDelegate = parentDelegate
        mMvpDelegate.onCreate()
        mMvpDelegate.onAttach()
        return this
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        btnNext.setOnClickListener { mCallback?.invoke(NEXT_CLICK) }
        switchTouchId.setOnClickListener {
            if (switchTouchId.isChecked) mCallback?.invoke(TOUCH_ID_CLICK)
        }
        if (FingerprintUtil.checkSensorState(context!!) != FingerprintUtil.SensorState.READY) {
            touchGroup.visibility = View.GONE
            touchDivider.visibility = View.GONE
        }
    }

    fun startAnimation(direction: AnimationDirection, listener: Animator.AnimatorListener? = null) {
        val alphaFrom = if (direction == AnimationDirection.TO_LEFT || direction == AnimationDirection.TO_RIGHT) 1f else 0f
        val alphaTo = if (direction == AnimationDirection.TO_LEFT || direction == AnimationDirection.TO_RIGHT) 0f else 1f
        val positionFrom = when (direction) {
            AnimationDirection.FROM_LEFT -> -measuredWidth.toFloat()
            AnimationDirection.TO_LEFT -> 0f
            AnimationDirection.FROM_RIGHT -> measuredWidth.toFloat()
            AnimationDirection.TO_RIGHT -> 0f
        }
        val positionTo = when (direction) {
            AnimationDirection.FROM_LEFT -> 0f
            AnimationDirection.TO_LEFT -> -measuredWidth.toFloat()
            AnimationDirection.FROM_RIGHT -> 0f
            AnimationDirection.TO_RIGHT -> measuredWidth.toFloat()
        }

        val animatorSet = AnimatorSet()
        val titleAnimator = ObjectAnimator.ofFloat(tvTitle, "alpha", alphaFrom, alphaTo)
        titleAnimator.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val buttonAnimator = ObjectAnimator.ofFloat(btnNext, "alpha", alphaFrom, alphaTo)
        buttonAnimator.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val formAnimator = ObjectAnimator.ofFloat(formContainer, "x", positionFrom, positionTo)
        if (listener != null) animatorSet.addListener(listener)
        animatorSet.playTogether(titleAnimator, buttonAnimator, formAnimator)
        animatorSet.start()
    }

}