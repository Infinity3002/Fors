package proglife.fora.bank.ui.message.chats

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.li_chat.view.*
import proglife.fora.bank.R
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import proglife.fora.bank.features.chat.models.Chat
import proglife.fora.bank.utils.loadImage
import proglife.fora.bank.widgets.SwipeListener


class ChatsAdapter(private val mChatsListener: ChatsListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chats: List<Chat> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0)
            return SearchHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_search_message, parent, false))
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_chat, parent, false))
    }

    override fun getItemCount(): Int {
        return chats.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) holder.bind(chats[holder.adapterPosition - 1])
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    fun setItems(list: List<Chat>) {
        this.chats = list
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPhoto: ImageView = itemView.ivPhoto
        private val tvName: TextView = itemView.tvName
        private val tvMessage: TextView = itemView.tvMessage
        private val tvCount: TextView = itemView.tvCount
        private val tvDate: TextView = itemView.tvDate
        private val btnDelete = itemView.btnDelete

        @SuppressLint("SetTextI18n")
        fun bind(chat: Chat) {
            itemView.setOnClickListener { mChatsListener.onSelect(chat) }
            itemView.setOnTouchListener(SwipeListener(btnDelete))
            ivPhoto.loadImage(chat.photoUrl) {
                circleCrop()
            }
            tvName.text = "${chat.firstName} ${chat.lastName}"
            tvMessage.text = chat.message
            tvDate.text = chat.date
            tvCount.text = chat.unreadCount.toString()
            tvCount.visibility = if (chat.unreadCount > 0) View.VISIBLE else View.GONE
        }
    }

    class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface ChatsListener {
        fun onSelect(chat: Chat)
    }

}