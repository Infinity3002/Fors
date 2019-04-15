package proglife.fora.bank.ui.finances.bills.statement.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.li_finance_history_group.view.*
import proglife.fora.bank.features.bills.models.StatementGroup
import proglife.fora.bank.ui.finances.bills.statement.BillStatementsAdapter
import proglife.fora.bank.ui.finances.history.FinanceHistoryAdapter
import proglife.fora.bank.ui.finances.history.HistoryGroup

class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvDate: TextView = itemView.tvDate
    private val tvAmount: TextView = itemView.tvAmount

    fun bind(item: StatementGroup, onStatementActionListener: BillStatementsAdapter.OnStatementActionListener) {
        tvDate.text = item.date
        tvAmount.text = item.amount
    }

}