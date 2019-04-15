package proglife.fora.bank.ui.feed.pages.settings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.li_settings.view.*
import proglife.fora.bank.R

class FeedSettingsAdapter(val items: Array<Settings>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0)
            return TopHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_top_text, parent, false))
        if(viewType == 1)
            return TitleHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_header, parent, false))
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_settings, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is ItemHolder -> {
                holder.ivIcon.setImageResource(item.resIcon)
                holder.tvTitle.setText(item.resName)


            }
            is TitleHolder -> {
                holder.tvTitle.setText(items[position].resName)
            }
            is TopHolder -> holder.tvTop.setText(items[position].resName)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) 0
        else if(items[position] == Settings.TITLE_PAYMENT_SERVICES
                || items[position] == Settings.TITLE_APP
                || items[position] == Settings.TITLE_CURRENT
                || items[position] == Settings.TITLE_UPCOMING) 1
        else 2
    }

    class TopHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTop = itemView as TextView
    }
    class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle = itemView as TextView
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvTitle = itemView.tvTitle!!
        val switchView = itemView.switchView!!
        val ivIcon = itemView.ivIcon!!
    }

    enum class Settings(val resIcon: Int, val resName: Int, var isSelect: Boolean = false) {
        TITLE_TOP(0, R.string.select_news),
        TITLE_APP(0, R.string.title_app),
        TITLE_PAYMENT_SERVICES(0, R.string.title_info),
        TITLE_CURRENT(0, R.string.title_current),
        TITLE_UPCOMING(0, R.string.title_upcoming),
        CHECKS(R.drawable.ic_settings_checks, R.string.checks),
        DEPOSITS(R.drawable.ic_settings_deposits, R.string.deposits),
        CARD(R.drawable.ic_settings_cards, R.string.card),
        DEBENTURE_STOCK(R.drawable.ic_settings_debenture_stock, R.string.debenture_stock),
        CELLS(R.drawable.ic_settings_cells, R.string.сells),
        PAYMENTS_AND_TRANSFERS(R.drawable.ic_settings_taxi, R.string.payments_and_transfers),
        CREDITS(R.drawable.ic_settings_credits, R.string.credits),
        SERVICES(R.drawable.ic_settings_services, R.string.services),
        TAXI(R.drawable.ic_settings_taxi, R.string.taxi),
        MOVIE_TICKETS(R.drawable.ic_settings_movie, R.string.movie_tickets),
        PROMOTION_STORES(R.drawable.ic_settings_shop, R.string.promotion_stores),
        RESERVATION_HOTEL(R.drawable.ic_settings_hotel, R.string.reservation_hotel),
        AIR_TICKETS(R.drawable.ic_settings_train, R.string.air_tickets),
        INSURANCE(R.drawable.ic_settings_insurance, R.string.insurance),
        NEWS_BANK(R.drawable.ic_settings_news_bank, R.string.news_bank),
        CHANGE_WORK(R.drawable.ic_settings_time, R.string.change_work),
        RECOMMENDATIONS(R.drawable.ic_settings_recommend, R.string.recommendations),
        EXCHANGE_RATES(R.drawable.ic_settings_exchange_rates, R.string.exchange_rates),
        NEW_APP(R.drawable.ic_settings_new, R.string.new_app);

        companion object {
            fun noAuthUser() = arrayOf(
                    TITLE_TOP,
                    TITLE_APP,
                    TAXI,
                    MOVIE_TICKETS,
                    PROMOTION_STORES,
                    RESERVATION_HOTEL,
                    AIR_TICKETS,
                    INSURANCE,
                    TITLE_PAYMENT_SERVICES,
                    NEWS_BANK,
                    CHANGE_WORK,
                    RECOMMENDATIONS,
                    EXCHANGE_RATES,
                    NEW_APP
            )

            fun authUser() = arrayOf(
                    TITLE_TOP,
                    TITLE_CURRENT,
                    CHECKS,
                    DEPOSITS,
                    CARD,
                    CELLS,
                    TITLE_UPCOMING,
                    PAYMENTS_AND_TRANSFERS,
                    CREDITS,
                    SERVICES,
                    TITLE_APP,
                    TAXI,
                    MOVIE_TICKETS,
                    PROMOTION_STORES,
                    RESERVATION_HOTEL,
                    AIR_TICKETS,
                    INSURANCE,
                    TITLE_PAYMENT_SERVICES,
                    NEWS_BANK,
                    CHANGE_WORK,
                    RECOMMENDATIONS,
                    EXCHANGE_RATES,
                    NEW_APP
            )
        }
    }
}