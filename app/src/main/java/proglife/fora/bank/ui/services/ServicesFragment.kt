package proglife.fora.bank.ui.services

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_services.*
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.R
import proglife.fora.bank.navigation.Screens.GOOGLE_MAP
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.utils.DefaultAnimatorListener
import proglife.fora.bank.widgets.TopDividerDecoration

class ServicesFragment : BaseFragment() {

    private lateinit var adapter: ServicesAdapter

    private var isFirstRun = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ServicesAdapter({}, {
            (activity as MainActivity).router.navigateTo(GOOGLE_MAP)
//            val animation = AnimationUtils.loadAnimation(context, R.anim.top_in)
//            animation.setAnimationListener(object : Animation.AnimationListener {
//                override fun onAnimationRepeat(animation: Animation?) {
//
//                }
//
//                override fun onAnimationEnd(animation: Animation?) {
//                    (activity as MainActivity).router.navigateTo(GOOGLE_MAP)
//                }
//
//                override fun onAnimationStart(animation: Animation?) {
//                }
//
//            })
//            rvServices.startAnimation(animation)

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_services, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvServices.adapter = adapter
        rvServices.addItemDecoration(TopDividerDecoration(ContextCompat.getDrawable(activity!!, R.drawable.drawable_divider)!!))

        if (isFirstRun) {
            isFirstRun = false
            rvServices.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_in))
        }
    }

}