package proglife.fora.bank.ui.finances.history

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import proglife.fora.bank.R
import proglife.fora.bank.features.finance.models.History
import proglife.fora.bank.ui.finances.history.holders.GroupViewHolder
import proglife.fora.bank.ui.finances.history.holders.HeaderViewHolder
import proglife.fora.bank.ui.finances.history.holders.ItemViewHolder
import proglife.fora.bank.utils.inflate

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FinanceHistoryAdapter(private val onHistoryActionListener: OnHistoryActionListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_GROUP = 1
        private const val TYPE_ITEM = 2
    }

    private var items: List<*> = emptyList<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.li_finance_history_header))
            TYPE_GROUP -> GroupViewHolder(parent.inflate(R.layout.li_finance_history_group))
            TYPE_ITEM -> ItemViewHolder(parent.inflate(R.layout.li_finance_history_item))
            else -> throw IllegalArgumentException("Unsupported view type $viewType")
        }
    }

    override fun getItemCount(): Int = items.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? HeaderViewHolder)?.bind(onHistoryActionListener)
        (holder as? GroupViewHolder)?.bind(items[position - 1] as HistoryGroup, onHistoryActionListener)
        (holder as? ItemViewHolder)?.bind(items[position - 1] as History, onHistoryActionListener)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            items[position - 1] is HistoryGroup -> TYPE_GROUP
            items[position - 1] is History -> TYPE_ITEM
            else -> throw IllegalArgumentException("Can't get item view type for position $position")
        }
    }

    fun setItems(list: List<*>) {
        this.items = list
    }

    interface OnHistoryActionListener {
        fun onSearchHistory(text: String)
        fun onSelectHistory(history: History)
    }

}