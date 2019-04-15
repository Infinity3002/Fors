package proglife.fora.bank.ui.feed

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_feed_host.*
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.widgets.ToolbarHelper
import proglife.fora.bank.widgets.arctabs.ToolbarItem
import android.view.MotionEvent
import com.arellomobile.mvp.presenter.InjectPresenter
import proglife.fora.bank.ui.feed.pages.feed.FeedPageFragment
import proglife.fora.bank.ui.feed.pages.info.FeedInfoFragment
import proglife.fora.bank.ui.feed.pages.offers.FeedOffersFragment
import proglife.fora.bank.ui.feed.pages.settings.FeedSettingsFragment


/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FeedHostFragment : BaseFragment(), FeedHostView {

    @InjectPresenter
    lateinit var mPresenter: FeedHostPresenter
    private lateinit var gestureDetector : GestureDetector

    private var toolbarHelper : ToolbarHelper? = null
    private var isAuth: Boolean = false
    private var fragments: List<Fragment> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100
            private var swiped = false

            override fun onDown(e: MotionEvent): Boolean {
                swiped = false
                return true
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                var result = false
                if (swiped) return result
                try {
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                            result = true
                        }
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }

                return result
            }

            fun onSwipeRight() {
                swiped = true
                toolbarHelper?.previous()
            }

            fun onSwipeLeft() {
                swiped = true
                toolbarHelper?.next()
            }

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_host, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        overlay.setOnTouchListener { v, event ->
            flHost.dispatchTouchEvent(event)
            gestureDetector.onTouchEvent(event)
        }

        (context as MainActivity).bottomBarState(false)
    }

    private var selectedToolbarItem: ToolbarItem? = null

    private fun onSelect(toolbarItem: ToolbarItem) {
        if (toolbarItem != selectedToolbarItem) {
            selectedToolbarItem = toolbarItem
            val itemIndex = getFragmentIndex(toolbarItem.section)
            showFragment(fragments[itemIndex])
            val colors = when (toolbarItem.section) {
                ToolbarItem.Section.FEED_CURRENT -> intArrayOf(Color.parseColor("#ED4949"), Color.parseColor("#F1B074"))
                ToolbarItem.Section.FEED_UPCOMING -> intArrayOf(Color.parseColor("#ED4949"), Color.parseColor("#F1B074"))
                ToolbarItem.Section.NEWS_OFFERS -> intArrayOf(Color.parseColor("#ED4949"), Color.parseColor("#F1B074"))
                ToolbarItem.Section.NEWS_INFO -> intArrayOf(Color.parseColor("#F1B074"), Color.parseColor("#ED8353"))
                ToolbarItem.Section.NEWS_SETTINGS -> intArrayOf(Color.parseColor("#ED8353"), Color.parseColor("#ED4949"))
                else -> intArrayOf(0,0)
            }
            toolbar?.setColors(colors, true)
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transition = childFragmentManager.beginTransaction()
        transition.setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out)
        transition
                .replace(R.id.flHost, fragment)
                .commit()
    }

    enum class FeedType {
        CURRENT,
        UPCOMING
    }

    override fun init(isAuth: Boolean) {
        this.isAuth = isAuth
        fragments = if (isAuth) {
            listOf(
                    FeedPageFragment.newInstance(FeedType.CURRENT),
                    FeedPageFragment.newInstance(FeedType.UPCOMING),
                    FeedOffersFragment.newInstance(),
                    FeedInfoFragment.newInstance(),
                    FeedSettingsFragment.newInstance()
            )
        } else {
            listOf(
                    FeedOffersFragment.newInstance(),
                    FeedInfoFragment.newInstance()
            )
        }
        val list = if (isAuth) {
            listOf(
                    ToolbarItem(ToolbarItem.Section.FEED_CURRENT, getString(R.string.feed_current)),
                    ToolbarItem(ToolbarItem.Section.FEED_UPCOMING, getString(R.string.feed_upcoming)),
                    ToolbarItem(ToolbarItem.Section.NEWS_OFFERS, getString(R.string.feed_news)),
                    ToolbarItem(ToolbarItem.Section.NEWS_INFO, getString(R.string.feed_info)),
                    ToolbarItem(ToolbarItem.Section.NEWS_SETTINGS, icon = R.drawable.selector_feed_settings)
            )
        } else {
            listOf(
                    ToolbarItem(ToolbarItem.Section.NEWS_OFFERS, getString(R.string.feed_news)),
                    ToolbarItem(ToolbarItem.Section.NEWS_INFO, getString(R.string.feed_info))
            )
        }
        toolbarHelper = ToolbarHelper(
                rvToolbar,
                list,
                selectListener = this::onSelect
        )
    }

    private fun getFragmentIndex(section: ToolbarItem.Section): Int {
        if (isAuth) {
            return when (section) {
                ToolbarItem.Section.FEED_CURRENT -> 0
                ToolbarItem.Section.FEED_UPCOMING -> 1
                ToolbarItem.Section.NEWS_OFFERS -> 2
                ToolbarItem.Section.NEWS_INFO -> 3
                ToolbarItem.Section.NEWS_SETTINGS -> 4
                else -> 0
            }
        } else {
            return when (section) {
                ToolbarItem.Section.NEWS_OFFERS -> 0
                ToolbarItem.Section.NEWS_INFO -> 1
                else -> 0
            }
        }
    }

}