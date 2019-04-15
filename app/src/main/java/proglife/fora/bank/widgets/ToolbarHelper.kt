package proglife.fora.bank.widgets

import android.app.Activity
import android.graphics.Color
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import proglife.fora.bank.R
import proglife.fora.bank.widgets.arctabs.CurvedTextView
import proglife.fora.bank.widgets.arctabs.ToolbarAdapter
import proglife.fora.bank.widgets.arctabs.ToolbarItem
import proglife.fora.bank.widgets.arctabs.TurnLayoutManager

/**
 * Created by Evhenyi Shcherbyna on 13.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ToolbarHelper(
        private val recyclerView: RecyclerView,
        private val items: List<ToolbarItem>,
        scrollPercentage: ((Float) -> Unit)? = null,
        selectListener: ((ToolbarItem) -> Unit)? = null,
        var textColors: IntArray = intArrayOf(Color.parseColor("#bbffffff"), Color.WHITE)
) {

    private val context = recyclerView.context
    private val radius = context.resources.getDimensionPixelOffset(R.dimen._360sdp) * 2
    private val peekDistance = context.resources.getDimensionPixelOffset(R.dimen._18sdp)
    private val snapHelper = LinearSnapHelper()
    private var lastSnapView: View? = null
    private val adapter: ToolbarAdapter

    init {
        val layoutManger = TurnLayoutManager(context,
                TurnLayoutManager.Gravity.END,
                TurnLayoutManager.Orientation.HORIZONTAL,
                radius,
                peekDistance,
                true)
        recyclerView.layoutManager = layoutManger

        adapter = ToolbarAdapter(items, textColors) { itemView, _ ->
            val x = snapHelper.calculateDistanceToFinalSnap(recyclerView.layoutManager, itemView)!![0]
            recyclerView.smoothScrollBy(x, 0)
        }
        recyclerView.adapter = adapter
        recyclerView.setItemViewCacheSize(8)
        recyclerView.setHasFixedSize(true)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val snapView = snapHelper.findSnapView(recyclerView.layoutManager)
                if (lastSnapView != snapView) {
                    lastSnapView?.let {
                        if (it is CurvedTextView) {
                            it.setColor(textColors[0])
                        }
                        if (it is ImageView) {
                            it.isActivated = false
                        }
                    }
                    snapView?.let {
                        if (it is CurvedTextView) {
                            it.setColor(textColors[1], "sans-serif-medium")
                        }
                        if (it is ImageView) {
                            it.isActivated = true
                        }
                    }
                    lastSnapView = snapView
                }

                val maxScroll = recyclerView.computeHorizontalScrollRange()
                val currentScroll = recyclerView.computeHorizontalScrollOffset() + recyclerView.computeHorizontalScrollExtent()
                if (maxScroll != 0) {
                    scrollPercentage?.invoke(currentScroll.toFloat() / maxScroll)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView != null && lastSnapView != null) {
                    selectListener?.invoke(adapter.getItem(recyclerView.layoutManager.getPosition(lastSnapView)))
                }
            }
        })

//        val size = Point()
//        (context as Activity).windowManager.defaultDisplay.getSize(size)
//        val offset = size.x.shr(1)
//        recyclerView.setPadding(offset, 0, offset, 0)


        val size = Point()
        (context as Activity).windowManager.defaultDisplay.getSize(size)
        val offset = size.x.shr(1)
        recyclerView.setPadding(offset, 0, offset, 0)
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.post {
//            val firstOffset = recyclerView.findViewHolderForAdapterPosition(0).itemView.measuredWidth.shr(1)
//            val lastOffset = recyclerView.findViewHolderForAdapterPosition(statementAdapter.itemCount - 1).itemView.measuredWidth.shr(1)

//            snapHelper.attachToRecyclerView(recyclerView)
            recyclerView.smoothScrollBy(1, 0)
//            selectListener?.invoke(statementAdapter.getItem(0))
        }
    }

    fun next() {
        val maxPosition = recyclerView.adapter.itemCount - 1
        val currentPosition = recyclerView.layoutManager.getPosition(lastSnapView)
        if (currentPosition < maxPosition) {
            val distance = snapHelper.calculateDistanceToFinalSnap(
                    recyclerView.layoutManager,
                    recyclerView.findViewHolderForAdapterPosition(currentPosition + 1)!!.itemView
            )
            recyclerView.smoothScrollBy(distance?.get(0) ?: 0, 0)
        }
    }

    fun previous() {
        val currentPosition = recyclerView.layoutManager.getPosition(lastSnapView)
        if (currentPosition > 0) {
            val distance = snapHelper.calculateDistanceToFinalSnap(
                    recyclerView.layoutManager,
                    recyclerView.findViewHolderForAdapterPosition(currentPosition - 1)!!.itemView
            )
            recyclerView.smoothScrollBy(distance?.get(0) ?: 0, 0)
        }
    }

    fun updateTextColors(textColors: IntArray) {
        this.textColors = textColors
        adapter.textColors = textColors
        adapter.notifyDataSetChanged()
    }

}