package proglife.fora.bank.ui.auth.reg.views

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpDelegate
import com.redmadrobot.inputmask.MaskedTextChangedListener
import io.card.payment.CreditCard
import kotlinx.android.synthetic.main.fragment_auth_reg_form.view.*
import kotlinx.android.synthetic.main.view_reg_empty_card.view.*
import kotlinx.android.synthetic.main.view_reg_filled_card.view.*
import kotlinx.android.synthetic.main.view_reg_scope.view.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.auth.reg.models.CardFM
import proglife.fora.bank.features.card.CardUtils
import proglife.fora.bank.features.card.CardValidator
import proglife.fora.bank.features.card.models.CardBank
import proglife.fora.bank.features.card.models.CardBrand
import proglife.fora.bank.utils.error.Bag
import proglife.fora.bank.utils.hideKeyboard
import proglife.fora.bank.utils.inflate
import timber.log.Timber


class RegFormView(context: Context, attributeSet: AttributeSet) :
        FrameLayout(context, attributeSet) {

    private lateinit var mParentDelegate: MvpDelegate<*>
    private val mMvpDelegate: MvpDelegate<RegFormView> by lazy {
        val value = MvpDelegate(this)
        value.setParentDelegate(mParentDelegate, id.toString())
        value
    }

    private var mOnFormActionListener: OnFormActionListener? = null
    private var inEditMode: Boolean = true

    init {
        inflate(R.layout.fragment_auth_reg_form, true)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mMvpDelegate.onSaveInstanceState()
        mMvpDelegate.onDetach()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // DEFAULT
        vfTypes.displayedChild = 0
        btnCard.isActivated = true

        val listener = MaskedTextChangedListener(
                "[0000] [0000] [0000] [0000]",
                etEmptyCardNumber,
                null
        )

        etEmptyCardNumber.addTextChangedListener(listener)
        etEmptyCardNumber.onFocusChangeListener = listener

        val listenerNumber = MaskedTextChangedListener(
                "[000].[00].[000].[0].[0000].[0000000]",
                etScope,
                null
        )

        etScope.addTextChangedListener(listenerNumber)
        etScope.onFocusChangeListener = listenerNumber

        arrayOf(etEmptyCardNumber, etEmptyCardMonth, etEmptyCardYear, etEmptyCardCvv).forEach {
            it.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    onCardChanged()
//                    checkCard()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        val buttons = arrayOf<View>(btnCard, btnScore, btnContract)
        buttons.forEach { view ->
            view.setOnClickListener { pressedView ->
                buttons.forEach { it.isActivated = it == pressedView }
                val index = when (pressedView) {
                    btnCard -> {
                        btnNext.visibility = if (inEditMode) View.GONE else View.VISIBLE
                        tvHint.visibility = if (inEditMode) View.VISIBLE else View.GONE
                        0
                    }
                    btnScore -> {
                        btnNext.visibility = View.VISIBLE
                        tvHint.visibility = View.GONE
                        1
                    }
                    btnContract -> {
                        btnNext.visibility = View.VISIBLE
                        tvHint.visibility = View.GONE
                        2
                    }
                    else -> 0
                }
                if (index != vfTypes.displayedChild) vfTypes.displayedChild = index
            }
        }

        btnNext.setOnClickListener {
            mOnFormActionListener?.onCardConfirmed()
        }

        etEmptyCardNumber.setText("4256 9010 5003 1063")
        etEmptyCardMonth.setText("10")
    }

    fun init(parentDelegate: MvpDelegate<*>, onFormActionListener: OnFormActionListener): RegFormView {
        mParentDelegate = parentDelegate
        mOnFormActionListener = onFormActionListener
        mMvpDelegate.onCreate()
        mMvpDelegate.onAttach()

        return this
    }

    fun onCardChanged() {
        val currentFM = CardFM(
                etEmptyCardNumber.text.toString().replace("\\D".toRegex(), ""),
                etEmptyCardMonth.text.toString(),
                etEmptyCardYear.text.toString(),
                etEmptyCardCvv.text.toString()
        )
        mOnFormActionListener?.onCardPrepare(currentFM)
    }

    fun anim(fromShowToEdit: Boolean) {
        if (!fromShowToEdit) (context as Activity).hideKeyboard()
        vfCard.displayedChild = if (fromShowToEdit) 0 else 1
        tvHint.visibility = if (fromShowToEdit) View.VISIBLE else View.GONE
        btnNext.visibility = if (fromShowToEdit) View.GONE else View.VISIBLE
    }

    private fun buttonAnim(fromShowToEdit: Boolean){
        tvHint.startAnimation(AnimationUtils.loadAnimation(context, if (fromShowToEdit) R.anim.fade_in else R.anim.fade_out))
        if (fromShowToEdit) btnNext.visibility = View.GONE
        if (!fromShowToEdit) btnNext.visibility = View.VISIBLE
    }

    fun onBack(): Boolean {
        return if (inEditMode) {
            false
        } else {
            anim(true)
            inEditMode = true
            true
        }
    }

    fun startAnimation(hide: Boolean, listener : Animator.AnimatorListener? = null) {
        val alphaFrom = if (hide) 1f else 0f
        val alphaTo = if (hide) 0f else 1f
        val positionFrom = if (hide) 0f else -measuredWidth.toFloat()
        val positionTo = if (hide) -measuredWidth.toFloat() else 0f

        val animatorSet = AnimatorSet()
        val tabsAnimator = ObjectAnimator.ofFloat(llTabs, "alpha", alphaFrom, alphaTo)
        tabsAnimator.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val buttonAnimator = ObjectAnimator.ofFloat(btnNext, "alpha", alphaFrom, alphaTo)
        buttonAnimator.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        val formAnimator = ObjectAnimator.ofFloat(vfTypes, "x", positionFrom, positionTo)
        if (listener != null) animatorSet.addListener(listener)
        animatorSet.playTogether(tabsAnimator, buttonAnimator, formAnimator)
        animatorSet.start()
    }

    @SuppressLint("SetTextI18n")
    fun onScanResult(scanResult: CreditCard) {
        etEmptyCardNumber.setText(scanResult.cardNumber)

        val month = scanResult.expiryMonth
        if (month != 0) {
            etEmptyCardMonth.setText((if (month < 10) "0" else "") + month)
        }
        val year = scanResult.expiryYear
        if (year != 0) {
            if (year > 99) {
                val yearString = year.toString()
                etEmptyCardYear.setText(yearString.substring(yearString.lastIndex - 1, yearString.lastIndex))
            } else {
                etEmptyCardYear.setText((if (year < 10) "0" else "") + scanResult.expiryMonth)
            }
        }
    }

    fun showCard(cardInfo: Pair<CardFM, Pair<CardBrand?, CardBank?>>) {
        inEditMode = false
        etEmptyCardCvv.text.clear()
        etEmptyCardMonth.text.clear()
        etEmptyCardYear.text.clear()

        val card = cardInfo.first
        val cardBrand = cardInfo.second.first
        val cardBank = cardInfo.second.second

        tvNumberCard.text = CardUtils.maskCard(card.number)
        tvMonthValidity.text = card.monthExp
        tvYearValidity.text = card.yearExp
        tvCVV.text = "***"

        if (cardBrand != null) {
            try {
                val stream = context.assets.open(CardUtils.BRANDS_PATH + cardBrand.resource)
                val drawable = Drawable.createFromStream(stream, cardBrand.name)
                ivTypeCard.setImageDrawable(drawable)
            } catch (e : RuntimeException) {
                Timber.i(e)
                ivTypeCard.setImageDrawable(null)
            }
        } else {
            ivTypeCard.setImageDrawable(null)
        }

        if (cardBank != null) {
            val gradientColors = cardBank.backgroundColors.map { Color.parseColor(it) }.toIntArray()
            val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors)
            filledCardContainer.background = gradientDrawable

            try {
                val stream = context.assets.open(CardUtils.BANKS_PATH + cardBank.alias + ".png")
                val drawable = Drawable.createFromStream(stream, cardBank.alias)
                ivLogoBank.setImageDrawable(drawable)
            } catch (e : RuntimeException) {
                Timber.i(e)
                ivLogoBank.setImageDrawable(null)
            }

            val textColor = Color.parseColor(cardBank.textColor)
            tvNumberCard.setTextColor(textColor)
            tvMonthValidity.setTextColor(textColor)
            tvYearValidity.setTextColor(textColor)
            tvCVV.setTextColor(textColor)
        } else {
            filledCardContainer.setBackgroundResource(R.drawable.gradient_red)
            ivLogoBank.setImageDrawable(null)

            tvNumberCard.setTextColor(Color.WHITE)
            tvMonthValidity.setTextColor(Color.WHITE)
            tvYearValidity.setTextColor(Color.WHITE)
            tvCVV.setTextColor(Color.WHITE)
        }

        anim(inEditMode)
    }

    fun showValidationError(bag: Bag) {
        tvEmptyCardLabel.isActivated = bag.contains(CardValidator.CARD_NUMBER) && !etEmptyCardNumber.text.isEmpty()
        tvEmptyCardValidityLabel.isActivated = bag.contains(CardValidator.CARD_MONTH_EXP) && !etEmptyCardMonth.text.isEmpty()
                || bag.contains(CardValidator.CARD_YEAR_EXP) && !etEmptyCardYear.text.isEmpty()
        tvEmptyCardCvvLabel.isActivated = bag.contains(CardValidator.CARD_CVV) && !etEmptyCardCvv.text.isEmpty()
    }

    interface OnFormActionListener {
        fun onCardPrepare(cardFM: CardFM)
        fun onCardConfirmed()
    }

}