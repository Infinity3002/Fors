package proglife.fora.bank.ui.message.chats

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_chats.*
import proglife.fora.bank.R
import proglife.fora.bank.features.chat.models.Chat
import proglife.fora.bank.ui.base.BaseFragment


class ChatsFragment : BaseFragment(), ChatsView {

    @InjectPresenter
    lateinit var mPresenter: ChatsPresenter

    private lateinit var adapter: ChatsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ChatsAdapter(object : ChatsAdapter.ChatsListener {
            override fun onSelect(chat: Chat) {
                mPresenter.select(chat)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMessages.adapter = adapter
        rvMessages.addItemDecoration(ChatDividerDecoration(ContextCompat.getDrawable(context!!, R.drawable.drawable_divider)!!))
        rvMessages.layoutManager = LinearLayoutManager(context)


        btnBack.setOnClickListener { mPresenter.back() }
        rvMessages.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_in))

        bottomBarState(true)
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun showChats(list: List<Chat>) {
        adapter.setItems(list)
        adapter.notifyDataSetChanged()
    }

//    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator {
////        println("onCreateAnimator $transit $enter $nextAnim ${rvMessages.translationX}")
//        val width = contentLayout.measuredWidth.toFloat()
//        val animator = if (enter) {
//            ValueAnimator.ofFloat(width, 0f)
//        } else {
//            ValueAnimator.ofFloat(0f, -width)
//        }
//
//        animator.setDuration(500)
//
//        animator.addUpdateListener {
//            println("Chats ${contentLayout.translationX}")
//            contentLayout.translationX = it.animatedValue as Float
//        }
//        return animator
//    }

}