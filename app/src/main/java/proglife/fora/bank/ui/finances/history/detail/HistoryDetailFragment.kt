package proglife.fora.bank.ui.finances.history.detail

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_history_detail.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class HistoryDetailFragment : BaseFragment(), HistoryDetailView {

    companion object {
        fun newInstance(): HistoryDetailFragment = HistoryDetailFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: HistoryDetailPresenter
    private lateinit var mAdapter: HistoryDetailActionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = HistoryDetailActionAdapter { }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomBarState(true)

        tvAmount.text = "-5000 \u20BD"

        btnBack.setOnClickListener { mPresenter.back() }

        sauronLayout.apply {
            alpha = 0f
            scaleY = 0.8f
            animate().alpha(1f).scaleY(1f).setStartDelay(200).setDuration(300)
            contentLayout.post {
                contentLayout.translationX = contentLayout.measuredWidth.toFloat()
                contentLayout.alpha = 1f
                contentLayout.animate().translationX(0f).setStartDelay(300).setDuration(400)
            }
        }

        val collapsedViews = arrayOf(ivLogo, tvTitle, tvAmount)
        appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val factor = Math.abs(verticalOffset.toFloat() / appBarLayout.totalScrollRange)
            collapsedViews.forEach {
                it.scaleX = 0.7f + (1.0f - factor) * 0.3f
                it.scaleY = 0.7f + (1.0f - factor) * 0.3f
                it.alpha = 1.0f - factor
            }
        }

        rvMenu.apply {
            layoutManager = LinearLayoutManager(context)
            val dividerDrawable: Drawable = ContextCompat.getDrawable(context!!, R.drawable.drawable_divider)!!
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                    c.save()
                    val childCount = parent.childCount
                    val padding = context.resources.getDimensionPixelOffset(R.dimen._15sdp)
                    for (i in 0 until childCount) {
                        val child: View = parent.getChildAt(i)
                        if (parent.getChildAdapterPosition(child) == 0) continue
                        val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
                        val top = child.top + params.topMargin
                        val bottom = top + dividerDrawable.intrinsicHeight
                        dividerDrawable.setBounds(parent.paddingLeft + padding, top,
                                parent.width - parent.paddingRight - padding, bottom)
                        dividerDrawable.draw(c)
                    }
                    c.restore()
                }
            })
            adapter = mAdapter
        }
    }

}