package proglife.fora.bank.ui.finances.cards.statement.list

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.li_date_statement.view.*
import kotlinx.android.synthetic.main.li_statement.view.*
import proglife.fora.bank.R
import proglife.fora.bank.models.Statement
import java.text.SimpleDateFormat
import java.util.*

class CardStatementAdapter(var statements: List<Statement>, val onStatementActionListener: OnStatementActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var TYPE_SEARCH = 0
    var TYPE_DATE = 1
    var TYPE_STATEMENT = 2
    lateinit var prevStatement : Statement

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_SEARCH -> SearchHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_search_statement, parent, false))
            TYPE_DATE -> DateHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_date_statement, parent, false))
            else -> ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_statement, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return statements.size * 2 + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ItemHolder -> {


                var statement = getItem(holder.adapterPosition  / 2 - 1)
                holder.bind(statement)

            }

            is DateHolder -> {

                var pos = holder.adapterPosition  / 2

                var statement = getItem(pos)
                prevStatement = statement
                if(pos != 0) {
                    prevStatement = getItem((pos - 1))
                    holder.bind(statement, prevStatement)
                } else {
                    holder.bind(statement)
                }


            }
            else -> {

            }
        }
    }

    private fun getItem(position: Int) : Statement {
        return statements[position]
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_SEARCH
            position % 2 == 1 -> TYPE_DATE
            else -> TYPE_STATEMENT
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.tvTitle
        private val tvSubTitle: TextView = itemView.tvSubTitle
        private val ivLogo: ImageView = itemView.ivLogo
        private val rvContainer: ConstraintLayout = itemView.rvContainer
        private val wrap: LinearLayout = itemView.wrap

        fun bind(statement: Statement) {
            tvTitle.text = statement.title
            tvSubTitle.text = statement.subTitle
            ivLogo.setImageResource(statement.image)

            rvContainer.setOnClickListener {
//                val expanded = statement.expand
//                statement.expand = !expanded
                onStatementActionListener.onSelect(statement)
            }

            if(!statement.expand) {
                wrap.visibility = View.GONE
            } else {
                wrap.visibility = View.VISIBLE
            }

        }
    }

    class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class DateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvDate: TextView = itemView.tvDate
        private val tvMoney: TextView = itemView.tvMoney
        fun bind(statement: Statement, prevStatement: Statement) {
            if(prevStatement.date != statement.date) {
                tvDate.visibility = View.VISIBLE
                tvMoney.visibility = View.VISIBLE
                tvDate.text = getConvertDate(statement.date)
            }
        }

        fun bind(statement: Statement) {
                tvDate.visibility = View.VISIBLE
                tvMoney.visibility = View.VISIBLE
                tvDate.text = getConvertDate(statement.date)
        }

        private fun getConvertDate(time : Long) : String {

            var sdf = SimpleDateFormat("dd MMMM", Locale.ENGLISH)
            var date = Date(time)

            return sdf.format(date)
        }
    }

    interface OnStatementActionListener {
        fun onSelect(statement: Statement)
    }


}