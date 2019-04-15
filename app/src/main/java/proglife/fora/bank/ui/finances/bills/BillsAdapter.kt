package proglife.fora.bank.ui.finances.bills

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.li_bill.view.*
import kotlinx.android.synthetic.main.li_bill_footer.view.*
import proglife.fora.bank.R
import proglife.fora.bank.features.bills.models.Bill
import proglife.fora.bank.utils.inflate
import proglife.fora.bank.utils.toMoney

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BillsAdapter(private val onBillActionListener: OnBillActionListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_FOOTER = 1
    }

    private var items: List<Bill> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_FOOTER -> FooterViewHolder(parent.inflate(R.layout.li_bill_footer))
            TYPE_ITEM -> BillViewHolder(parent.inflate(R.layout.li_bill))
            else -> throw IllegalArgumentException("Unsupported view type $viewType")
        }
    }

    override fun getItemCount(): Int = items.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? BillViewHolder)?.bind(items[holder.adapterPosition], onBillActionListener)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> TYPE_FOOTER
            else -> TYPE_ITEM
        }
    }

    fun setItems(list: List<Bill>) {
        this.items = list
    }

    class BillViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val tvName: TextView = itemView.tvName
        private val tvNumber: TextView = itemView.tvNumber
        private val tvBalance: TextView = itemView.tvBalance
        private val btnSend: Button = itemView.btnSend
        private val btnRefill: Button = itemView.btnRefill
        private val clCenter: View = itemView.clCenter

        fun bind(bill: Bill, onBillActionListener: OnBillActionListener) {
            tvName.text = bill.name
            tvNumber.text = bill.number
            tvBalance.text = toMoney(bill.balance)
            btnSend.setOnClickListener { onBillActionListener.onSendBill(bill) }
            btnRefill.setOnClickListener { onBillActionListener.onRefillBill(bill) }
            clCenter.setOnClickListener { onBillActionListener.onBillSelect(bill) }
        }

    }

    class FooterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val btnAdd: Button = itemView.btnAdd

        fun bind(onBillActionListener: OnBillActionListener) {
            btnAdd.setOnClickListener { onBillActionListener.onAddBill() }
        }
    }

    interface OnBillActionListener {
        fun onBillSelect(bill: Bill)
        fun onRefillBill(bill: Bill)
        fun onSendBill(bill: Bill)
        fun onAddBill()
    }

}