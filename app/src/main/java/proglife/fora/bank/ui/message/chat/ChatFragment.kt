package proglife.fora.bank.ui.message.chat

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_chat.*
import proglife.fora.bank.R
import proglife.fora.bank.features.chat.models.Chat
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.utils.blockButton
import proglife.fora.bank.utils.loadImage
import java.util.*

class ChatFragment: BaseFragment(), ChatView {

    companion object {
        private const val CHAT = "chat"

        fun newInstance(chat: Chat): ChatFragment {
            val bundle = Bundle()
            bundle.putParcelable(CHAT, chat)
            val fragment = ChatFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mPresenter: ChatPresenter

    @ProvidePresenter
    fun providePresenter(): ChatPresenter = ChatPresenter(arguments!!.getParcelable(CHAT) as Chat)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance()
        val calendar1 = Calendar.getInstance()
        calendar1.set(Calendar.DAY_OF_MONTH, 18)
        val calendar2 = Calendar.getInstance()
        calendar2.set(Calendar.DAY_OF_MONTH, 19)
        val list: List<Any> = mutableListOf(
                TempChat("Занесу когда нибудь",calendar.timeInMillis, TempChat.State.REQUEST_SUCCESS),
                TempChat("Занесу когда нибудь",calendar.timeInMillis, TempChat.State.REQUEST),
                TempChat("Занесу когда нибудь",calendar.timeInMillis, TempChat.State.CURRENT_USER),
                TempGroup(calendar.timeInMillis),
                TempChat("500 руб.",calendar2.timeInMillis, TempChat.State.USER),
                TempChat("А сколько вышло?",calendar2.timeInMillis, TempChat.State.CURRENT_USER),
                TempChat("Переведешь, как обещал?",calendar2.timeInMillis, TempChat.State.USER),
                TempGroup(calendar2.timeInMillis),
                TempChat("Отдыхаю после вчерашнего",calendar1.timeInMillis, TempChat.State.CURRENT_USER),
                TempChat("Привет, как дела?",calendar1.timeInMillis, TempChat.State.USER),
                TempGroup(calendar1.timeInMillis)
        )

        (context as MainActivity).bottomBarState(true)
        rvChat.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        rvChat.adapter = ChatAdapter(list,  (activity as MainActivity).router)

        ivOptionalMenu.setOnClickListener{
            llOptional.visibility = if(llOptional.visibility == View.GONE) View.VISIBLE else View.GONE
            it.isSelected = llOptional.visibility == View.VISIBLE
        }

        btnSend.setOnClickListener {
            it.blockButton()
            (activity as MainActivity).router.navigateTo(Screens.CHAT_REQUEST, Screens.CHAT)
        }

        btnBack.setOnClickListener{(activity as MainActivity).onBackPressed() }

        rvChat.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_in))
    }

    override fun showChat(chat: Chat) {
        tvToolbarTitle.setText("${chat.firstName} ${chat.lastName}")
        ivPhoto.loadImage(chat.photoUrl) { circleCrop() }
    }


    override fun onPause() {
        super.onPause()
        val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etMessage.windowToken, 0)
    }

//    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator {
//        val display = (context as MainActivity).windowManager.defaultDisplay
//        val position = IntArray(2)
//        contentLayout.getLocationInWindow(position)
//        val size = Point()
//        display.getSize(size)
//        val values: Pair<Float, Float> = when {
//            enter && contentLayout.translationX < 0 -> -1f to 0f
//            enter -> 1f to 0f
//            else -> 0f to 1f
//        }
//
//        contentLayout.translationX = contentLayout.width * values.first
//
//        val animator = ValueAnimator.ofFloat(values.first, values.second)
//                .setDuration(500)
//        animator.addUpdateListener {
//            contentLayout.translationX = it.animatedValue as Float * size.x
//        }
//        return animator
//    }

}