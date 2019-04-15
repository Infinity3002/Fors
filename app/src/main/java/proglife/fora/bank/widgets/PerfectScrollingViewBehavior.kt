package proglife.fora.bank.widgets

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.WindowInsetsCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import proglife.fora.bank.R

/**
 * Created by Evhenyi Shcherbyna on 22.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class PerfectScrollingViewBehavior @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null): AppBarLayout.ScrollingViewBehavior(context, attrs) {

    private val arcHeight = context.resources.getDimensionPixelOffset(R.dimen.arc_height)
    private var topInset = 0

    override fun onLayoutChild(parent: CoordinatorLayout?, child: View, layoutDirection: Int): Boolean {
        val res = super.onLayoutChild(parent, child, layoutDirection)
        child.top = child.top - arcHeight - topInset
        child.bottom = child.bottom
        return res
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        super.onDependentViewChanged(parent, child, dependency)
        child.top = child.top - arcHeight - topInset
        return false
    }

    override fun onApplyWindowInsets(coordinatorLayout: CoordinatorLayout?, child: View?, insets: WindowInsetsCompat?): WindowInsetsCompat {
        val result = super.onApplyWindowInsets(coordinatorLayout, child, insets)
        topInset = insets?.systemWindowInsetTop ?: 0
        return result
    }

    override fun onMeasureChild(parent: CoordinatorLayout?, child: View?,
                                parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int,
                                heightUsed: Int): Boolean {
        val childLpHeight = child!!.layoutParams.height
        if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT || childLpHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // If the menu's height is set to match_parent/wrap_content then measure it
            // with the maximum visible height

            val dependencies = parent!!.getDependencies(child)
            val header = findFirstDependency(dependencies)
            if (header != null) {
                if (ViewCompat.getFitsSystemWindows(header) && !ViewCompat.getFitsSystemWindows(child)) {
                    // If the header is fitting system windows then we need to also,
                    // otherwise we'll get CoL's compatible measuring
                    child.fitsSystemWindows = true

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        // If the set succeeded, trigger a new layout and return true
                        child.requestLayout()
                        return true
                    }
                }

                var availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec)
                if (availableHeight == 0) {
                    // If the measure spec doesn't specify a size, use the current height
                    availableHeight = parent.height
                }

                val height = availableHeight - header.measuredHeight + getScrollRange(header) + arcHeight + topInset
                val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                        if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT)
                            View.MeasureSpec.EXACTLY
                        else
                            View.MeasureSpec.AT_MOST)

                // Now measure the scrolling view with the correct height
                parent.onMeasureChild(child, parentWidthMeasureSpec,
                        widthUsed, heightMeasureSpec, heightUsed)

                return true
            }
        }
        return false
    }

    private fun getScrollRange(v: View): Int {
        return (v as? AppBarLayout)?.totalScrollRange ?: v.measuredHeight
    }

    private fun findFirstDependency(views: List<View>): AppBarLayout? {
        var i = 0
        val z = views.size
        while (i < z) {
            val view = views[i]
            if (view is AppBarLayout) {
                return view
            }
            i++
        }
        return null
    }

}