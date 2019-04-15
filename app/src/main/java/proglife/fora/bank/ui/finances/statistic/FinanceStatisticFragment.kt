package proglife.fora.bank.ui.finances.statistic

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Point
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.AnimationSet
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_finance_statistic.*
import org.threeten.bp.LocalDate
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.finances.statistic.chart.Bubble
import proglife.fora.bank.ui.finances.statistic.chart.BubblesView
import proglife.fora.bank.ui.finances.statistic.chart.BubblesView2
import proglife.fora.bank.utils.OnBackPressedListener

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FinanceStatisticFragment: BaseFragment(), FinanceStatisticView, OnBackPressedListener {

    companion object {
        fun newInstance(): FinanceStatisticFragment = FinanceStatisticFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: FinanceStatisticPresenter
    lateinit var mAdapter: MonthsAdapter
    lateinit var mSnapHelper: LinearSnapHelper
    var mSnapView: View? = null
    private val animationSet = AnimatorSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = MonthsAdapter { view: View, _: Int, _: LocalDate ->
            val x = mSnapHelper.calculateDistanceToFinalSnap(rvMonths.layoutManager, view)!![0]
            rvMonths.smoothScrollBy(x, 0)
        }

        val now = LocalDate.now().withDayOfMonth(1)
        mAdapter.setItems(IntArray(12).mapIndexed { index, _ ->  now.minusMonths(index.toLong()) })
        mSnapHelper = LinearSnapHelper()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finance_statistic, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bubblesView.onItemActionListener = object : BubblesView.OnItemActionListener, BubblesView2.OnItemActionListener {
            override fun onOpen(bubble: Bubble) {
//                overlay.visibility = View.VISIBLE
                mPresenter.open(bubble)
            }
        }
        val size = Point()
        activity!!.windowManager.defaultDisplay.getSize(size)
        val offset = size.x / 2 - resources.getDimensionPixelOffset(R.dimen._30sdp)
        rvMonths.setPadding(offset, 0, offset, 0)
        rvMonths.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        rvMonths.adapter = mAdapter
        mSnapHelper.attachToRecyclerView(rvMonths)
        rvMonths.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val snapView = mSnapHelper.findSnapView(recyclerView.layoutManager)
                if (mSnapView != snapView) {
                    mSnapView?.isActivated = false
                    snapView?.isActivated = true
                    mSnapView = snapView
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView != null && mSnapView != null) {
                    mPresenter.selectMonth(mAdapter.getItem(recyclerView.layoutManager.getPosition(mSnapView)))
                }
            }

        })
        btnDebit.isActivated = true
        arrayOf(btnDebit, btnCredit).forEach { button ->
            button.setOnClickListener {
                if ((it == btnCredit && !btnCredit.isActivated) || (it == btnDebit && !btnDebit.isActivated)) {
                    resetChart()
                }
                btnDebit.isActivated = it == btnDebit
                btnCredit.isActivated = it == btnCredit
            }
        }
    }

    override fun onBackPressed(): Boolean {
        return !bubblesView.back()
    }

    override fun showMonth(date: LocalDate) {
       resetChart()
    }

    private fun resetChart() {
        animationSet.cancel()
        animationSet.duration = 300
        val fadeOut = ObjectAnimator.ofFloat(bubblesView, "alpha", 1f, 0f)
        fadeOut.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                bubblesView.reset()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
        val fadeIn = ObjectAnimator.ofFloat(bubblesView, "alpha", 0f, 1f)
        animationSet.playSequentially(fadeOut, fadeIn)
        animationSet.start()
    }

}