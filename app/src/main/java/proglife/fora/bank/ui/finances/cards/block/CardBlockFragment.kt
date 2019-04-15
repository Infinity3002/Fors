package proglife.fora.bank.ui.finances.cards.block

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_card_block.*
import kotlinx.android.synthetic.main.li_card_for_list.*
import proglife.fora.bank.R
import proglife.fora.bank.features.card.CardUtils
import proglife.fora.bank.features.card.models.CardWithInfo
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.finances.cards.detail.CardDetailFragment
import proglife.fora.bank.ui.finances.cards.list.CardsFragment
import proglife.fora.bank.ui.finances.cards.list.CardsFragment.Companion.CARD_VIEW
import proglife.fora.bank.utils.isVisibly
import timber.log.Timber

/**
 * Created by Evhenyi Shcherbyna on 23.08.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CardBlockFragment : BaseFragment(), CardBlockView {

    companion object {
        private const val CARD_WITH_INFO = "card_with_info"

        fun newInstance(cardWithInfo: CardWithInfo): CardBlockFragment {
            val fragment = CardBlockFragment()
            val bundle = Bundle()
            bundle.putParcelable(CARD_WITH_INFO, cardWithInfo)
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mPresenter: CardBlockPresenter

    @ProvidePresenter
    fun providePresenter(): CardBlockPresenter {
        return CardBlockPresenter(arguments!![CARD_WITH_INFO] as CardWithInfo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_block, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bottomBarState(true)
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnLock.setOnClickListener { mPresenter.lock() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val data = arguments!![CARD_WITH_INFO] as CardWithInfo
        cardView.transitionName = CardsFragment.CARD_VIEW + data.card.id
    }

    @SuppressLint("SetTextI18n")
    override fun showCard(cardWithInfo: CardWithInfo) {
        val card = cardWithInfo.card
        val brand = cardWithInfo.brand
        val bank = cardWithInfo.bank

        tvNumber.text = card.number
        tvDate.text = "${card.expireMonth}/${card.expireYear}"
        tvBalance.text = context?.getString(R.string.card_balance, card.balance)

        if (bank != null) {
            val gradientColors = bank.backgroundColors.map { Color.parseColor(it) }.toIntArray()
            val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors)
            ivBackground.setImageDrawable(gradientDrawable)

            try {
                val stream = context!!.assets.open(CardUtils.BANKS_PATH + bank.alias + ".png")
                val drawable = Drawable.createFromStream(stream, bank.alias)
                ivLogoBank.setImageDrawable(drawable)
            } catch (e : RuntimeException) {
                Timber.i(e)
            }

            val textColor = Color.parseColor(bank.textColor)
            tvNumber.setTextColor(textColor)
            tvName.setTextColor(textColor)
            tvBalance.setTextColor(textColor)
            tvDate.setTextColor(textColor)
        } else {
            tvName.text = card.name
        }

        if (brand != null) {
            try {
                val stream = context!!.assets.open(CardUtils.BRANDS_PATH + brand.resource)
                val drawable = Drawable.createFromStream(stream, brand.name)
                ivBrand.setImageDrawable(drawable)
            } catch (e : RuntimeException) {
                Timber.i(e)
            }
        }
    }
}