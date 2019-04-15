package proglife.fora.bank.utils

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import proglife.fora.bank.R

/**
 * Created by Evhenyi Shcherbyna on 08.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ItemOverDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val arcHeight = context.resources.getDimensionPixelOffset(R.dimen._20sdp)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = -arcHeight
    }


}