package proglife.fora.bank.ui.feed.pages.feed

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import proglife.fora.bank.utils.inflate

/**
 * Created by Evhenyi Shcherbyna on 07.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FeedItemAdapter(
        private val items: List<Int>
) : RecyclerView.Adapter<FeedItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent.inflate(viewType))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return items[position]
    }

    class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Any) {
        }
    }

}