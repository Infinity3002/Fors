package proglife.fora.bank.ui.message.chats

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import proglife.fora.bank.R


class ChatDividerDecoration(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {

    private var left = -1
    private var right = -1

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)
        drawVertical(c, parent)
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        if (left == -1) {
            left = parent.paddingLeft + parent.context.resources.getDimensionPixelSize(R.dimen._19sdp)
            right = parent.width - parent.paddingRight - parent.context.resources.getDimensionPixelSize(R.dimen._20sdp)
        }

        val childCount = parent.childCount
        for (i in 2 until childCount) {
            val child: View = parent.getChildAt(i)
            val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.top + params.topMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }
}