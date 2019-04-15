package proglife.fora.bank.ui.services

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.li_icon_menu.view.*
import kotlinx.android.synthetic.main.li_search.view.*
import proglife.fora.bank.R

class ServicesAdapter(
        private val callback: (Service) -> Unit,
        private val searchCallback: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = Service.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0)
            return SearchHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_search, parent, false))
        if (viewType == 1)
            return HeaderHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_header, parent, false))
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_icon_menu, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemHolder -> holder.bind(items[position - 1])
            is HeaderHolder -> holder.bind(items[position - 1])
            is SearchHolder -> holder.bind(searchCallback)
        }
        holder.itemView.setOnClickListener { callback.invoke(items[holder.adapterPosition - 1]) }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            1 -> 1
            5 -> 1
            else -> 2
        }
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivIcon = itemView.ivIcon as ImageView
        val tvTitle = itemView.tvTitle as TextView

        fun bind(item: Service) {
            ivIcon.setImageResource(item.resIcon)
            tvTitle.setText(item.resName)
        }
    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView as TextView

        fun bind(item: Service) {
            tvTitle.setText(item.resName)
        }
    }

    class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etSearch = itemView.etSearch as EditText
        val btnSearch = itemView.btnSearch as View

        fun bind(searchCallback: (String) -> Unit) {
            btnSearch.setOnClickListener { searchCallback.invoke(etSearch.text.toString()) }
        }
    }

    enum class Service(val resIcon: Int, val resName: Int) {
        TITLE_SERVICE_FORABANK(0, R.string.title_service_foraback),
        DEPARTMENTS(R.drawable.ic_departments, R.string.departments),
        CASH_MACHINE(R.drawable.ic_cash_machine, R.string.cash_machine),
        PAYMENT_DEVICES(R.drawable.ic_payment_devices, R.string.payment_devices),
        TITLE_SERVICE_PARTNER(0, R.string.title_partner_services),

        PAYMENTS_AND_TRANSFERS(R.drawable.ic_settings_taxi, R.string.payments_and_transfers),
        MOVIE_TICKETS(R.drawable.ic_settings_movie, R.string.movie_tickets),
        VIEW_TV(R.drawable.ic_pmt_internet, R.string.view_tv),
        DISCOUNTS_SHOPS(R.drawable.ic_discount, R.string.discounts_shops),
        RESERVATION_HOTEL(R.drawable.ic_settings_hotel, R.string.reservation_hotel),
        AIR_TICKETS(R.drawable.ic_settings_train, R.string.air_tickets),
        INSURANCE(R.drawable.ic_settings_insurance, R.string.insurance);

    }

}