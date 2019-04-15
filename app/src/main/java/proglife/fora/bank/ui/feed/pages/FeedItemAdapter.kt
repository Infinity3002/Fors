package proglife.fora.bank.ui.feed.pages

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.li_feed_item.view.*
import proglife.fora.bank.R
import proglife.fora.bank.models.NewsItem
import proglife.fora.bank.utils.inflate
import proglife.fora.bank.utils.loadImage

/**
 * Created by Evhenyi Shcherbyna on 07.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FeedItemAdapter(diffCallback : DiffUtil.ItemCallback<NewsItem>) : ListAdapter<NewsItem, FeedItemAdapter.NewsItemViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewsItemViewHolder(parent.inflate(R.layout.li_feed_item))

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NewsItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val backgroundImage : ImageView = itemView.ivBackground
        private val btnAction : Button = itemView.btnAction
        private val tvDescription : TextView = itemView.tvDescription

        fun bind(item: NewsItem) {
            backgroundImage.loadImage(item.backgroundImageUrl) {
                centerCrop()
            }
            btnAction.visibility = if (item.buttonText.isBlank()) View.GONE else View.VISIBLE
            btnAction.text = item.buttonText
            tvDescription.text = item.descriptionText
        }

    }

}