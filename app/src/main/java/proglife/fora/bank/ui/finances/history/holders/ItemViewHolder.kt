package proglife.fora.bank.ui.finances.history.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.li_finance_history_item.view.*
import proglife.fora.bank.features.finance.models.History
import proglife.fora.bank.ui.finances.history.FinanceHistoryAdapter
import proglife.fora.bank.utils.loadImage

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivLogo: ImageView = itemView.ivLogo
    private val tvName: TextView = itemView.tvName
    private val tvInfo: TextView = itemView.tvInfo
    private val tvAmount: TextView = itemView.tvAmount

    fun bind(history: History, onHistoryActionListener: FinanceHistoryAdapter.OnHistoryActionListener) {
        ivLogo.loadImage(history.logoUrl)
        tvName.text = history.name
        tvInfo.text = history.info
        tvAmount.text = history.amount
        itemView.setOnClickListener { onHistoryActionListener.onSelectHistory(history) }
    }

}