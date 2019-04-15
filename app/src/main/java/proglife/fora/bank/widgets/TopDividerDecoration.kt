package proglife.fora.bank.widgets

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import proglife.fora.bank.R


class TopDividerDecoration(private val mDivider: Drawable, private val startPosition:Int = 2) : RecyclerView.ItemDecoration() {

    private var left = -1
    private var right = -1

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)
        drawVertical(c, parent)
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        if (left == -1) {
            left = parent.paddingLeft + parent.context.resources.getDimensionPixelSize(R.dimen._15sdp)
            right = parent.width - parent.paddingRight - parent.context.resources.getDimensionPixelSize(R.dimen._15sdp)
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)

            if(parent.getChildAdapterPosition(child) < startPosition) continue
            val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.top + params.topMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}