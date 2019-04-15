package proglife.fora.bank.ui.finances.history.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.li_finance_history_group.view.*
import proglife.fora.bank.ui.finances.history.FinanceHistoryAdapter
import proglife.fora.bank.ui.finances.history.HistoryGroup

class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvDate: TextView = itemView.tvDate
    private val tvAmount: TextView = itemView.tvAmount

    fun bind(item: HistoryGroup, onHistoryActionListener: FinanceHistoryAdapter.OnHistoryActionListener) {
        tvDate.text = item.date
        tvAmount.text = item.amount
    }

}