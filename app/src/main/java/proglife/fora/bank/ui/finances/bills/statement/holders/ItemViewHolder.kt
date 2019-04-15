package proglife.fora.bank.ui.finances.bills.statement.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.li_finance_history_item.view.*
import proglife.fora.bank.features.finance.models.History
import proglife.fora.bank.models.Statement
import proglife.fora.bank.ui.finances.bills.statement.BillStatementsAdapter
import proglife.fora.bank.ui.finances.history.FinanceHistoryAdapter
import proglife.fora.bank.utils.loadImage

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivLogo: ImageView = itemView.ivLogo
    private val tvName: TextView = itemView.tvName
    private val tvInfo: TextView = itemView.tvInfo
    private val tvAmount: TextView = itemView.tvAmount

    fun bind(statement: Statement, onStatementActionListener: BillStatementsAdapter.OnStatementActionListener) {
        ivLogo.loadImage("http://webiconspng.com/wp-content/uploads/2016/11/cash_finance_money_money_in_wallet_wallet_icon_532652.png")
        tvName.text = statement.title
        tvInfo.text = statement.subTitle
        tvAmount.text = "5560,15 \u20BD"
        itemView.setOnClickListener { onStatementActionListener.onSelect(statement) }
    }

}