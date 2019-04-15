package proglife.fora.bank

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.li_icon_menu.view.*

class TestAdapter : RecyclerView.Adapter<TestAdapter.ItemHolder>() {

    val items = CardMenu.values()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_icon_menu, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        holder.tvTitle.setText(items[holder.adapterPosition].res)
        holder.ivIcon.setImageResource(items[holder.adapterPosition].resDrawable)
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.tvTitle!!
        val ivIcon = itemView.ivIcon!!
    }

    enum class CardMenu(@StringRes val res: Int, @DrawableRes val resDrawable: Int) {
        REFILL_DEPOSIT(R.string.refill_deposit, R.drawable.ic_refill_deposit),
        WITHDRAW_FUNDS(R.string.withdraw_funds, R.drawable.ic_withdraw_funds),
        SHARE_DETAILS(R.string.share_details, R.drawable.ic_shared),
        LINK_MAP(R.string.link_map, R.drawable.ic_tie_card),
        EXTEND_VALIDITY_PERIOD(R.string.extend_validity_period, R.drawable.ic_extend_period_validity),
        CLOSE_DEPOSIT(R.string.close_deposit, R.drawable.ic_lock)
    }
}