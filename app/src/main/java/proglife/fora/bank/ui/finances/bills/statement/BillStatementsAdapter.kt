package proglife.fora.bank.ui.finances.bills.statement

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import proglife.fora.bank.R
import proglife.fora.bank.features.bills.models.Bill
import proglife.fora.bank.features.bills.models.StatementGroup
import proglife.fora.bank.models.Statement
import proglife.fora.bank.ui.finances.bills.statement.holders.GroupViewHolder
import proglife.fora.bank.ui.finances.bills.statement.holders.HeaderViewHolder
import proglife.fora.bank.ui.finances.bills.statement.holders.ItemViewHolder
import proglife.fora.bank.utils.inflate

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BillStatementsAdapter(private val onStatementActionListener: OnStatementActionListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_GROUP = 1
        private const val TYPE_ITEM = 2
    }

    private var items: List<*> = emptyList<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.li_bill_statement_header))
            TYPE_GROUP -> GroupViewHolder(parent.inflate(R.layout.li_bill_statement_group))
            TYPE_ITEM -> ItemViewHolder(parent.inflate(R.layout.li_bill_statement_item))
            else -> throw IllegalArgumentException("Unsupported view type $viewType")
        }
    }

    override fun getItemCount(): Int = items.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? HeaderViewHolder)?.bind(onStatementActionListener)
        (holder as? GroupViewHolder)?.bind(items[position - 1] as StatementGroup, onStatementActionListener)
        (holder as? ItemViewHolder)?.bind(items[position - 1] as Statement, onStatementActionListener)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            items[position - 1] is StatementGroup-> TYPE_GROUP
            items[position - 1] is Statement -> TYPE_ITEM
            else -> throw IllegalArgumentException("Can't get item view type for position $position")
        }
    }

    fun setItems(list: List<*>) {
        this.items = list
    }

    interface OnStatementActionListener {
        fun onSearch(text: String)
        fun onSelect(statement: Statement)
    }

}