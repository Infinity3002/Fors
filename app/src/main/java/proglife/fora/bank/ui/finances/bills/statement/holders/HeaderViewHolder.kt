package proglife.fora.bank.ui.finances.bills.statement.holders

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.li_finance_history_header.view.*
import proglife.fora.bank.ui.finances.bills.statement.BillStatementsAdapter
import proglife.fora.bank.ui.finances.history.FinanceHistoryAdapter

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val etSearch: EditText = itemView.etSearch
    private var onStatementActionListener: BillStatementsAdapter.OnStatementActionListener? = null

    init {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onStatementActionListener?.onSearch(etSearch.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun bind(onStatementActionListener: BillStatementsAdapter.OnStatementActionListener) {
        this.onStatementActionListener = onStatementActionListener
    }
}