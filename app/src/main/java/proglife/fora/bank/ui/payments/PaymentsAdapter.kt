package proglife.fora.bank.ui.payments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.li_icon_menu.view.*
import proglife.fora.bank.R

class PaymentsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER_TEMPLATE = 0
    private val ITEM_TEMPLATE = 1
    private val BUTTON_TEMPLATE = 2
    private val HEADER = 3
    private val ITEM = 4

    private val items = Payments.values()
    private val itemsTemplate = Template.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER)
            return HeaderHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_header, parent, false))
        if(viewType == HEADER_TEMPLATE)
            return TemplateHeaderHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_header, parent, false))
        if(viewType == BUTTON_TEMPLATE)
            return TemplateButtonHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_bond_footer, parent, false))
        if(viewType == ITEM_TEMPLATE)
            return TemplateButtonHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_template, parent, false))
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_icon_menu, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size + itemsTemplate.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            holder.ivIcon.setImageResource(items[position - itemsTemplate.size ].resIcon)
            holder.tvTitle.setText(items[position - itemsTemplate.size ].resName)
        }
        if (holder is HeaderHolder) {
            holder.tvTitle.setText(items[position - itemsTemplate.size ].resName)
        }

        if (holder is TemplateHeaderHolder) {
            holder.tvTitle.setText(itemsTemplate[position].resName)
        }


    }

    override fun getItemViewType(position: Int): Int {
        val lastIndexTemp = if (itemsTemplate.isNotEmpty()) itemsTemplate.size  else 0
        return when {
            itemsTemplate.isNotEmpty() && position == 0 -> HEADER_TEMPLATE
            lastIndexTemp - position == 0 ||
                    lastIndexTemp - position == 7 -> HEADER
            lastIndexTemp - 1 == position -> BUTTON_TEMPLATE
            lastIndexTemp > position -> ITEM_TEMPLATE
            else -> ITEM
        }
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon = itemView.ivIcon!!
        val tvTitle = itemView.tvTitle!!
    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView as TextView
    }
    class TemplateHeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView as TextView
    }
    class TemplateButtonHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class TemplateItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    enum class Payments(val resIcon: Int, val resName: Int) {

        TITLE_TRANSLATIONS(0, R.string.title_translations),
        BETWEEN_Y_ACC(R.drawable.ic_pmt_between_self_acc, R.string.between_your_accounts),
        CLIENT_BANK(R.drawable.ic_pmt_bank_client, R.string.client_bank),
        OTHER_BANK(R.drawable.ic_pmt_transfer_to_bank, R.string.other_bank),
        REQUEST_MONEY_TO_ACC(R.drawable.ic_pmt_request_money, R.string.request_money_to_account),
        ELECTRONIC_WALLET(R.drawable.ic_pmt_to_e_wallet, R.string.electronic_wallet),
        CHARITY(R.drawable.ic_pmt_charity, R.string.charity),

        TITLE_PAYMENT_SERVICES(0, R.string.title_payment_services),
        MOBILE(R.drawable.ic_pmt_mobile_connection, R.string.mobile),
        MULCT(R.drawable.ic_pmt_mulct, R.string.mulct_or_taxes),
        PUBLISH_SERVICE(R.drawable.ic_pmt_publish_service, R.string.public_service),
        INTERNET(R.drawable.ic_pmt_internet, R.string.internet),
        SOCIAL_OR_GAME(R.drawable.ic_pmt_games, R.string.social_or_game),
        SECURITY(R.drawable.ic_pmt_security, R.string.security),
        TRANSPORT_CARD(R.drawable.ic_pmt_transport, R.string.transport_cart),
        MARKETING(R.drawable.ic_pmt_marketing, R.string.online_shop),
        OTHER(R.drawable.ic_pmt_other, R.string.other)

    }


    enum class Template(val resName: Int) {
        TITLE_TEMPLATE(R.string.title_template),
        TEMP_ALFA(R.string.title_template),
        ADD_TEMPLATE(R.string.title_template),
    }
}