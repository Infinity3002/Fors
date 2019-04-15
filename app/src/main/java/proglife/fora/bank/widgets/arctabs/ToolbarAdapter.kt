package proglife.fora.bank.widgets.arctabs

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.li_toolbar_image.view.*
import kotlinx.android.synthetic.main.li_toolbar_text.view.*
import proglife.fora.bank.R
import proglife.fora.bank.utils.inflate

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ToolbarAdapter(
        private val list: List<ToolbarItem>,
        var textColors: IntArray,
        private val onSelectItemListener: (View, ToolbarItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_TEXT = 1
        const val TYPE_IMAGE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_TEXT)
            TextViewHolder(parent.inflate(R.layout.li_toolbar_text))
        else
            ImageViewHolder(parent.inflate(R.layout.li_toolbar_image))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).icon == 0) TYPE_TEXT else TYPE_IMAGE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder) {
            is TextViewHolder -> {
                holder.title.setText(item.title)
                holder.title.setColor(textColors[0])
            }
            is ImageViewHolder -> holder.ivIcon.setImageResource(item.icon)
        }

        holder.itemView.setOnClickListener {
            onSelectItemListener.invoke(it, item)
        }
    }

    fun getItem(position: Int): ToolbarItem {
        return list[position]
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: CurvedTextView = view.tvTitle
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.ivIcon
    }

}