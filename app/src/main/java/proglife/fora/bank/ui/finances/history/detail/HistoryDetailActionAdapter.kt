package proglife.fora.bank.ui.finances.history.detail

import android.graphics.Color
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.li_icon_menu.view.*
import proglife.fora.bank.R
import proglife.fora.bank.utils.inflate

/**
 * Created by Evhenyi Shcherbyna on 04.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class HistoryDetailActionAdapter(private val callback: (Menu) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = Menu.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(parent.inflate(R.layout.li_icon_menu))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ItemViewHolder)?.bind(items[position], callback)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.tvTitle
        private val ivIcon: ImageView = itemView.ivIcon

        fun bind(item: Menu, callback: (Menu) -> Unit) {
            ivIcon.setImageResource(item.iconRes)
            tvTitle.setText(item.stringRes)
            tvTitle.setTextColor(Color.WHITE)
            itemView.setOnClickListener { callback.invoke(item) }
        }
    }

    enum class Menu(@DrawableRes val iconRes: Int, @StringRes val stringRes: Int) {
        DISTRIBUTE(R.drawable.ic_raspredelit, R.string.finance_history_distribute),
        CREATE_TEMPLATE(R.drawable.ic_create_template, R.string.finance_history_create_template),
        RETRY_TRANSFER(R.drawable.ic_repeat_transfer, R.string.finance_history_repeat_transfer),
        SHARED_LINK(R.drawable.ic_shared_link, R.string.finance_history_shared_link),
        REMOVE(R.drawable.ic_remove, R.string.finance_history_remove_item)
    }

}