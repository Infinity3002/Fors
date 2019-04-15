package proglife.fora.bank.ui.profile

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.li_icon_menu.view.*
import proglife.fora.bank.R

class ProfileControlAdapter(private val callback: (CardMenu) -> Unit) : RecyclerView.Adapter<ProfileControlAdapter.ItemHolder>() {

    private val items = CardMenu.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_icon_menu, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.tvTitle
        val ivIcon: ImageView = itemView.ivIcon

        fun bind(cardMenu: CardMenu) {
            tvTitle.setText(cardMenu.res)
            ivIcon.setImageResource(cardMenu.resDrawable)
            itemView.setOnClickListener { callback.invoke(cardMenu) }
        }

    }

    enum class CardMenu(@StringRes val res: Int, @DrawableRes val resDrawable: Int) {
        PROFILE_SETTING(R.string.profile_settings, R.drawable.ic_settings),
        PROFILE_MESSAGE(R.string.profile_message, R.drawable.ic_message),
        PROFILE_EXIT(R.string.profile_exit, R.drawable.ic_exit),
    }
}