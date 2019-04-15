package proglife.fora.bank.ui.message.success

import android.animation.Animator
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_auth_reg_sms.*
import kotlinx.android.synthetic.main.fragment_confirm_sms.*
import proglife.fora.bank.R
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.utils.DefaultAnimatorListener
import proglife.fora.bank.utils.OnBackPressedListener


class SuccessSMSFragment : BaseFragment(), OnBackPressedListener {

    companion object {
        private const val BACK_SCREEN = "back_screen"

        fun newInstance(backScreen: String): SuccessSMSFragment {
            val bundle = Bundle()
            bundle.putString(BACK_SCREEN, backScreen)
            val fragment = SuccessSMSFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var isAnimateBack = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_sms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etCode.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(smsCode: Editable) {
                if (smsCode.length == 4) {
                    val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(etCode.windowToken, 0)
                    val backScreen = arguments!!.getString(BACK_SCREEN)
                    when (backScreen) {
                        Screens.CHAT -> (activity as MainActivity).router.backTo(backScreen)
                        Screens.FINANCES -> (activity as MainActivity).router.navigateTo(Screens.TRANSFER_SUCCESS)
                    }
                }
            }
        })
        val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        etCode.requestFocus()
        btnBack.setOnClickListener { (activity as MainActivity).onBackPressed() }
    }

    override fun onBackPressed(): Boolean {
        if (!isAnimateBack) {
            val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etCode.windowToken, 0)
            isAnimateBack = true
            block.animate()
                    .translationX(block.width.toFloat())
                    .setListener(object : DefaultAnimatorListener() {

                        override fun onAnimationEnd(animation: Animator) {
                            if (activity != null)
                                (activity as MainActivity).router.exit()
                            isAnimateBack = false

                        }
                    })
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        block.postDelayed({
            block.translationX = block.measuredWidth.toFloat()
            block.visibility = View.VISIBLE
            block.animate()
                    .translationX(0f).start()
        }, 100)

    }
}