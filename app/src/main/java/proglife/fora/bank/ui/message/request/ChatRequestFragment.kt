package proglife.fora.bank.ui.message.request

import android.animation.Animator
import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.NOT_FOCUSABLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.fragment_chat_request.*
import proglife.fora.bank.R
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.utils.DefaultAnimatorListener
import proglife.fora.bank.utils.OnBackPressedListener
import proglife.fora.bank.utils.blockButton


class ChatRequestFragment : BaseFragment(), OnBackPressedListener {

    companion object {
        private const val BACK_SCREEN = "back_screen"
        private const val TITLE = "title"

        fun newInstance(backScreen: String): ChatRequestFragment {
            val bundle = Bundle()
            bundle.putString(BACK_SCREEN, backScreen)
            val fragment = ChatRequestFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun newInstanceRefill(backScreen: String): ChatRequestFragment {
            val bundle = Bundle()
            bundle.putString(BACK_SCREEN, backScreen)
            bundle.putInt(TITLE, R.string.refill_deposit)
            val fragment = ChatRequestFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var isAnimateBack = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).bottomBarState(true)
        spinnerCard.adapter = CardSpinnerAdapter(context!!)

        btnSend.setOnClickListener {
            it.blockButton()
            bottom.animate()
                    .translationY(if (bottom.translationY != 0f) 0f else bottom.height.toFloat())
                    .setListener(object : DefaultAnimatorListener() {
                        override fun onAnimationEnd(animation: Animator) {
                            (activity as MainActivity).router.navigateTo(Screens.CHAT_SUCCESS, arguments!!.getString(BACK_SCREEN))
                        }
                    })
            btnSend.animate()
                    .alpha(0f).start()
            etAmount.animate()
                    .alpha(0f).start()
        }

        tvToolbarTitle.setText(getString(arguments!!.getInt(TITLE, R.string.transation_request)))
        btnBack.setOnClickListener { (activity as MainActivity).onBackPressed() }

        etAmount.setOnEditorActionListener { v, actionId, event ->
            if (event?.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etAmount.windowToken, 0)
                etAmount.clearFocus()
            }
            false
        }
    }

    override fun onBackPressed(): Boolean {
        if(!isAnimateBack) {
            isAnimateBack = true
            btnSend.animate()
                    .alpha(0f).start()

            bottom.animate()
                    .translationY(bottom.height.toFloat())
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

    override fun onStart() {
        super.onStart()
        bottom.postDelayed({
            bottom.translationY = bottom.measuredHeight.toFloat()
            bottom.visibility = View.VISIBLE
            clContent.visibility = View.VISIBLE

            btnSend.animate()
                    .alpha(1f).start()
            etAmount.animate()
                    .alpha(1f).start()
            clContent.animate()
                    .alpha(1f).start()
            bottom.animate()
                    .translationY(0f).start()
        }, 100)

    }
}