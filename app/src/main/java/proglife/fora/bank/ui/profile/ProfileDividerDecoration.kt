package proglife.fora.bank.ui.profile

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View

class ProfileDividerDecoration (private val mDivider: Drawable, private val startPosition:Int = 2) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)
        drawVertical(c, parent)
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in startPosition until childCount) {
            val child: View = parent.getChildAt(i)
            val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.top + params.topMargin
            val bottom = top + mDivider.intrinsicHeight
            mDivider.setBounds(0, top, parent.width, bottom)
            mDivider.draw(c)
        }
    }
}