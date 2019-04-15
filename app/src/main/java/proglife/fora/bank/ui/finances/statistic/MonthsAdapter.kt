package proglife.fora.bank.ui.finances.statistic

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.li_statistic_month.view.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import proglife.fora.bank.R
import proglife.fora.bank.utils.inflate

/**
 * Created by Evhenyi Shcherbyna on 21.08.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class MonthsAdapter(val onSelectListener: (view: View, position: Int, date: LocalDate) -> Unit) : RecyclerView.Adapter<MonthsAdapter.MonthViewHolder>() {

    private val formatter = DateTimeFormatter.ofPattern("LLLL")
    private var items: List<LocalDate> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        return MonthViewHolder(parent.inflate(R.layout.li_statistic_month), onSelectListener)
    }

    override fun getItemCount(): Int = items.size

    fun getItem(position: Int): LocalDate = items[position]

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setItems(list: List<LocalDate>) {
        items = list
    }

    inner class MonthViewHolder(itemView: View, onSelectListener: (view: View, position: Int, date: LocalDate) -> Unit)
        : RecyclerView.ViewHolder(itemView) {

        private val tvMonth: TextView = itemView.tvMonth

        fun bind(localDate: LocalDate) {
            tvMonth.text = localDate.format(formatter)
            itemView.setOnClickListener { onSelectListener.invoke(itemView, adapterPosition, localDate) }
        }

    }

}