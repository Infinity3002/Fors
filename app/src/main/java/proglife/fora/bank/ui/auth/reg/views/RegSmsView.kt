package proglife.fora.bank.ui.auth.reg.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpDelegate
import kotlinx.android.synthetic.main.fragment_auth_reg_sms.view.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.auth.reg.models.CredentialsFM
import proglife.fora.bank.utils.AnimationDirection
import proglife.fora.bank.utils.hideKeyboard
import proglife.fora.bank.utils.inflate
import proglife.fora.bank.utils.showKeyboard


class RegSmsView(context: Context, attributeSet: AttributeSet) :
        FrameLayout(context, attributeSet) {

    private var mCallback: OnSMSListener? = null
    private lateinit var mParentDelegate: MvpDelegate<*>
    private val mMvpDelegate: MvpDelegate<RegSmsView> by lazy {
        val value = MvpDelegate(this)
        value.setParentDelegate(mParentDelegate, id.toString())
        value
    }

    init {
        inflate(R.layout.fragment_auth_reg_sms, true)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mMvpDelegate.onSaveInstanceState()
        mMvpDelegate.onDetach()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        etCode.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                mCallback?.onCheckSMS(s.toString())
            }
        })
        btnRetry.setOnClickListener {
            mCallback?.resend()
        }
    }

    fun init(parentDelegate: MvpDelegate<*>, callback : OnSMSListener): RegSmsView {
        mCallback = callback
        mParentDelegate = parentDelegate
        mMvpDelegate.onCreate()
        mMvpDelegate.onAttach()
        return this
    }

    fun startAnimation(direction: AnimationDirection, listener : Animator.AnimatorListener? = null) {
        if (direction == AnimationDirection.FROM_LEFT || direction == AnimationDirection.FROM_RIGHT) {
            etCode.showKeyboard()
        } else {
            (context as Activity).hideKeyboard()
        }

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
        val titleAnimator = ObjectAnimator.ofFloat(tvTitleInfo, "alpha", alphaFrom, alphaTo)
        titleAnimator.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val buttonAnimator = ObjectAnimator.ofFloat(btnRetry, "alpha", alphaFrom, alphaTo)
        buttonAnimator.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val formAnimator = ObjectAnimator.ofFloat(formContainer, "x", positionFrom, positionTo)
        if (listener != null) animatorSet.addListener(listener)
        animatorSet.playTogether(titleAnimator, buttonAnimator, formAnimator)
        animatorSet.start()
    }

    fun clearCode() {
        etCode.text.clear()
    }
    interface OnSMSListener {
        fun onCheckSMS(code: String)
        fun resend()
    }
}