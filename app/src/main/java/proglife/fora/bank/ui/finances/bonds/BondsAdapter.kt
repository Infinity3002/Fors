package proglife.fora.bank.ui.finances.bonds

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.li_bond_footer.view.*
import kotlinx.android.synthetic.main.li_bond_item.view.*
import kotlinx.android.synthetic.main.li_search.view.*
import proglife.fora.bank.R
import proglife.fora.bank.features.bonds.models.Bond
import proglife.fora.bank.utils.inflate
import proglife.fora.bank.utils.loadImage

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BondsAdapter(private val onBondActionListener: OnBondActionListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }

    private var items: List<Bond> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.li_bond_header))
            TYPE_ITEM -> BondViewHolder(parent.inflate(R.layout.li_bond_item))
            TYPE_FOOTER -> FooterViewHolder(parent.inflate(R.layout.li_bond_footer))
            else -> throw IllegalArgumentException("Unsupported view type $viewType")
        }
    }

    override fun getItemCount(): Int = items.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? HeaderViewHolder)?.bind(onBondActionListener)
        (holder as? BondViewHolder)?.bind(items[holder.adapterPosition - 1], onBondActionListener)
        (holder as? FooterViewHolder)?.bind(onBondActionListener)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            itemCount - 1 -> TYPE_FOOTER
            else -> TYPE_ITEM
        }
    }

    fun setItems(items: List<Bond>) {
        this.items = items
    }

    class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val etSearch: EditText = itemView.etSearch
        private var onSearchBondActionListener: OnBondActionListener? = null

        init {
            etSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    onSearchBondActionListener?.onSearchTyped(etSearch.text.toString())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        fun bind(onSearchBondActionListener: OnBondActionListener) {
            this.onSearchBondActionListener = onSearchBondActionListener
        }
    }

    class BondViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val ivLogo: ImageView = itemView.ivLogo
        private val tvName: TextView = itemView.tvName
        private val tvInfo: TextView = itemView.tvInfo
        private val tvPercent: TextView = itemView.tvPercent

        fun bind(bond: Bond, onBondActionListener: OnBondActionListener) {
            ivLogo.loadImage(bond.iconUrl)
            tvName.text = bond.name
            tvInfo.text = bond.info
            tvPercent.text = itemView.context.getString(R.string.all_percent, bond.percent)
            itemView.setOnClickListener { onBondActionListener.onBondSelect(bond) }
        }

    }

    class FooterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val btnBuy: Button = itemView.btnBuy

        fun bind(onBondActionListener: OnBondActionListener) {
            btnBuy.setOnClickListener { onBondActionListener.onBuyBond() }
        }
    }

    interface OnBondActionListener {
        fun onSearchTyped(text: String)
        fun onBuyBond()
        fun onBondSelect(bond: Bond)
    }

}