package proglife.fora.bank.ui.message.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_chat_request.view.*
import kotlinx.android.synthetic.main.li_message_current_user.view.*
import kotlinx.android.synthetic.main.li_message_user.view.*
import proglife.fora.bank.R
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.utils.blockButton
import ru.terrakok.cicerone.Router
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(val list: List<Any>, val router: Router) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DATE = 0
    private val USER = 1
    private val CURRENT_USER = 2
    private val REQUEST = 3
    private val REQUEST_SUCCESS = 4
    private val current = Calendar.getInstance()
    private val simpleDateFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is DateHolder && item is TempGroup) {

            val date = date(item.date)
            if (date != null) {
                (holder.itemView as TextView).text = date
                holder.itemView.visibility = View.VISIBLE
            }
        } else if (holder is CurrentUserHolder && item is TempChat) {
            holder.tvMessage.text = item.name
        } else if (holder is UserHolder && item is TempChat) {
            holder.tvMessage.text = item.name
        } else if (holder is RequestHolder) {
            holder.btnSend.setOnClickListener {
                it.blockButton()
                router.navigateTo(Screens.CHAT_REQUEST, Screens.CHAT)
            }
        }
    }

    private fun date(next: Long): String? {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = next
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        calendar.timeInMillis = System.currentTimeMillis()

        return if (day == current.get(Calendar.DAY_OF_MONTH) && year == current.get(Calendar.YEAR) && month == current.get(Calendar.MONTH)) {
            "Сегодня"
        } else {
            simpleDateFormat.format(Date(next))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == DATE)
            return DateHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_date, parent, false))
        if (viewType == CURRENT_USER)
            return CurrentUserHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_message_current_user, parent, false))
        if (viewType == USER)
            return UserHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_message_user, parent, false))
        if (viewType == REQUEST)
            return RequestHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_chat_payment_request, parent, false))
        return RequestSuccessHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_chat_request_success, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return when {
            item is TempGroup -> DATE
            item is TempChat && item.state == TempChat.State.REQUEST -> REQUEST
            item is TempChat && item.state == TempChat.State.REQUEST_SUCCESS -> REQUEST_SUCCESS
            item is TempChat && item.state == TempChat.State.USER -> USER
            else -> CURRENT_USER
        }
    }

    class DateHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class CurrentUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage = itemView.tvMessageCurrent!!
    }

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage = itemView.tvMessage!!
    }

    class RequestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnSend = itemView.btnSend!!
    }

    class RequestSuccessHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}