package proglife.fora.bank.ui.auth.reg.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.FrameLayout
import android.widget.Toast
import com.arellomobile.mvp.MvpDelegate
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.fragment_auth_reg_credentials.view.*
import proglife.fora.bank.R
import proglife.fora.bank.features.auth.AuthValidator
import proglife.fora.bank.ui.auth.reg.models.CredentialsFM
import proglife.fora.bank.utils.AnimationDirection
import proglife.fora.bank.utils.DefaultTextWather
import proglife.fora.bank.utils.error.Bag
import proglife.fora.bank.utils.inflate


/**
 * Created by Evhenyi Shcherbyna on 11.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class RegCredentialsView(context: Context, attributeSet: AttributeSet) :
        FrameLayout(context, attributeSet) {

    private var mOnCredentialsActionListener: OnCredentialsActionListener? = null

    private lateinit var mParentDelegate: MvpDelegate<*>
    private val mMvpDelegate: MvpDelegate<RegCredentialsView> by lazy {
        val value = MvpDelegate(this)
        value.setParentDelegate(mParentDelegate, id.toString())
        value
    }

    init {
        inflate(R.layout.fragment_auth_reg_credentials, true)
    }

    fun init(parentDelegate: MvpDelegate<*>, onCredentialsActionListener: OnCredentialsActionListener): RegCredentialsView {
        mOnCredentialsActionListener = onCredentialsActionListener
        mParentDelegate = parentDelegate
        mMvpDelegate.onCreate()
        mMvpDelegate.onAttach()
        return this
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mOnCredentialsActionListener?.onCheckPasswordStrength(etPassword.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        btnNext.setOnClickListener {
            mOnCredentialsActionListener?.onCheckCredentials(CredentialsFM(
                    etPhone.text.toString().replace(" ", "").replace("+", ""),
                    etLogin.text.toString(),
                    etPassword.text.toString(),
                    etRepeatPassword.text.toString()
            ))
        }

        cbAcceptAgreement.setOnCheckedChangeListener { _, isChecked ->
            btnNext.isEnabled = isChecked
        }

        val firstPart = context.getString(R.string.auth_reg_agreement)
        val linkPart = context.getString(R.string.auth_reg_agreement_link_part)
        val fullText = "$firstPart $linkPart"
        val startIndex = fullText.length - linkPart.length
        val endIndex = fullText.length

        val spannableContent = SpannableString(fullText)
        spannableContent.setSpan(UnderlineSpan(), startIndex, endIndex, 0)

        val span = object : ClickableSpan() {
            override fun onClick(view : View) {
                Toast.makeText(context, "Показ экрана условия банка", Toast.LENGTH_SHORT).show()
            }
        }

        spannableContent.setSpan(span, startIndex, endIndex, Spanned.SPAN_USER)
        spannableContent.setSpan(ForegroundColorSpan(Color.parseColor("#2f2f2f")), startIndex, endIndex, Spanned.SPAN_USER)

        tvConditionBank.text = spannableContent
        tvConditionBank.movementMethod = LinkMovementMethod.getInstance()

        etPassword.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            val level = v.background.level
            if (hasFocus && level == 0) v.background.level = 1
            else if (!hasFocus && level == 1) v.background.level = 0
        }

        val listener = MaskedTextChangedListener(
                "+7 [000] [000] [00] [00]",
                etPhone,
                null
        )

        etPhone.addTextChangedListener(listener)
        etPhone.onFocusChangeListener = listener

        etPhone.addTextChangedListener(object : DefaultTextWather(){
            override fun afterTextChanged(p0: Editable?) {
                etPhoneLayout.error = ""
            }
        })
        etLogin.addTextChangedListener(object : DefaultTextWather(){
            override fun afterTextChanged(p0: Editable?) {
                etLoginLayout.error = ""
            }
        })
        etPassword.addTextChangedListener(object : DefaultTextWather(){
            override fun afterTextChanged(p0: Editable?) {
                etPasswordLayout.error = ""
            }
        })
        etRepeatPassword.addTextChangedListener(object : DefaultTextWather(){
            override fun afterTextChanged(p0: Editable?) {
                etRepeatPasswordLayout.error = ""
            }
        })
        etPhone.setText("9260537633")
        etLogin.setText("kav")
        etPassword.setText("123456")
        etRepeatPassword.setText("123456")
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
        val titleAnimator = ObjectAnimator.ofFloat(tvTitleInfo, "alpha", alphaFrom, alphaTo)
        titleAnimator.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val buttonAnimator = ObjectAnimator.ofFloat(btnNext, "alpha", alphaFrom, alphaTo)
        buttonAnimator.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val formAnimator = ObjectAnimator.ofFloat(formContainer, "x", positionFrom, positionTo)
        if (listener != null) animatorSet.addListener(listener)
        animatorSet.playTogether(titleAnimator, buttonAnimator, formAnimator)
        animatorSet.start()
    }

    fun showPasswordStrength(strengthLevel: Int) {
        etPassword.background.level = strengthLevel + 1
        val pair = when (strengthLevel) {
            1 -> R.string.auth_reg_password_weak to R.color._ea4442
            2 -> R.string.auth_reg_password_medium to R.color._f1af73
            3 -> R.string.auth_reg_password_strong to R.color._22c183
            else -> R.string.auth_reg_create_password to R.color._9b9b9b
        }
        tvTitlePassword.setText(pair.first)
        tvTitlePassword.setTextColor(ContextCompat.getColor(context, pair.second))
    }

    fun clear() {
        etLogin.text.clear()
        etPassword.text.clear()
        etRepeatPassword.text.clear()
        cbAcceptAgreement.isChecked = false
    }

    fun showRegValidationError(bag: Bag) {
        etPhoneLayout.error = ""
        etLoginLayout.error = ""
        etPasswordLayout.error = ""
        etRepeatPasswordLayout.error = ""
        if(bag.containsKey(AuthValidator.PHONE)) etPhoneLayout.error = bag.getErrorString(context, AuthValidator.PHONE)
        if(bag.containsKey(AuthValidator.LOGIN)) etLoginLayout.error = bag.getErrorString(context, AuthValidator.LOGIN)
        if(bag.containsKey(AuthValidator.PASS)) etPasswordLayout.error = bag.getErrorString(context, AuthValidator.PASS)
        if(bag.containsKey(AuthValidator.CONFIRM_PASS)) etRepeatPasswordLayout.error = bag.getErrorString(context, AuthValidator.CONFIRM_PASS)
    }

    interface OnCredentialsActionListener {
        fun onCheckPasswordStrength(password: String)
        fun onCheckCredentials(credentialsFM: CredentialsFM)
    }

}