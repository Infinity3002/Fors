package proglife.fora.bank.ui.finances.cards.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.support.graphics.drawable.VectorDrawableCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_card_detail.*
import kotlinx.android.synthetic.main.li_card_for_detail.*
import kotlinx.android.synthetic.main.li_card_for_detail.view.*
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.R
import proglife.fora.bank.features.card.CardUtils
import proglife.fora.bank.features.card.models.CardWithInfo
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.finances.cards.control.CardControlFragment
import proglife.fora.bank.ui.finances.cards.list.CardsFragment
import proglife.fora.bank.widgets.ToolbarHelper
import proglife.fora.bank.widgets.arctabs.ToolbarItem
import timber.log.Timber
import proglife.fora.bank.ui.finances.cards.info.CardInfoFragment
import proglife.fora.bank.ui.finances.cards.statement.list.CardStatementFragment


/**
 * Created by Evhenyi Shcherbyna on 19.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CardDetailFragment : BaseFragment(), CardDetailView {

    companion object {
        private const val CARD_WITH_INFO = "card_with_info"

        fun newInstance(cardWithInfo: CardWithInfo): CardDetailFragment {
            val fragment = CardDetailFragment()
            val bundle = Bundle()
            bundle.putParcelable(CARD_WITH_INFO, cardWithInfo)
            fragment.arguments = bundle
            return fragment
        }

    }

    @InjectPresenter
    lateinit var mPresenter: CardDetailPresenter
    var mToolbarHelper: ToolbarHelper? = null

    private var isEnableBack = false

    @ProvidePresenter
    fun providePresenter(): CardDetailPresenter {
        return CardDetailPresenter(arguments!![CARD_WITH_INFO] as CardWithInfo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val data = arguments!![CARD_WITH_INFO] as CardWithInfo
        cardView.transitionName = CardsFragment.CARD_VIEW + data.card.id
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (context as MainActivity).bottomBarState(true)

        btnBack.setOnClickListener {
            mPresenter.back()
        }

        appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val factor = Math.abs(verticalOffset.toFloat() / appBarLayout.totalScrollRange)
            cardView.apply {
                scaleX = 0.7f + (1.0f - factor) * 0.3f
                scaleY = 0.7f + (1.0f - factor) * 0.3f
                alpha = 1.0f - factor
            }
        }

        mToolbarHelper = ToolbarHelper(
                rvToolbar,
                listOf(
                        ToolbarItem(ToolbarItem.Section.CARD_CONTROL, getString(R.string.card_control)),
                        ToolbarItem(ToolbarItem.Section.CARD_STATEMENT, getString(R.string.card_statement)),
                        ToolbarItem(ToolbarItem.Section.CARD_INFO, getString(R.string.card_info))
                ),
                selectListener = this::onSelect
        )
    }

    @SuppressLint("SetTextI18n")
    private fun makeCardView(view: View, cardWithInfo: CardWithInfo) {
        val card = cardWithInfo.card
        val brand = cardWithInfo.brand
        val bank = cardWithInfo.bank

        view.tvNumber.text = card.number
        view.tvDate.text = "${card.expireMonth}/${card.expireYear}"
        view.tvBalance.text = context?.getString(R.string.card_balance, card.balance)

        if (bank != null) {
            try {
                val stream = context!!.assets.open(CardUtils.BANKS_PATH + bank.alias + ".png")
                val drawable = Drawable.createFromStream(stream, bank.alias)
                view.ivLogoBank.setImageDrawable(drawable)
            } catch (e: RuntimeException) {
                Timber.i(e)
            }

            val textColor = Color.parseColor(bank.textColor)
            view.tvNumber.setTextColor(textColor)
            view.tvName.setTextColor(textColor)
            view.tvBalance.setTextColor(textColor)
            view.tvDate.setTextColor(textColor)
        } else {
            view.tvName.text = card.name
        }

        if (brand != null) {
            try {
                val stream = context!!.assets.open(CardUtils.BRANDS_PATH + brand.resource)
                val drawable = Drawable.createFromStream(stream, brand.name)
                view.ivBrand.setImageDrawable(drawable)
            } catch (e: RuntimeException) {
                Timber.i(e)
            }
        }

        view.tag = cardWithInfo
    }

    override fun showCard(cardWithInfo: CardWithInfo) {
        val colors = if (cardWithInfo.bank != null) {
            cardWithInfo.bank.backgroundColors.map { Color.parseColor(it) }.reversed().toIntArray()
        } else {
            resources.getStringArray(R.array.red_string_colors).map { Color.parseColor(it) }.toIntArray()
        }

        val textColors = if(cardWithInfo.bank != null) {
            val color = Color.parseColor(cardWithInfo.bank.textColor)
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)
            intArrayOf(Color.argb(190, red, green, blue), color)
        } else {
            intArrayOf(Color.parseColor("#bbffffff"), Color.WHITE)
        }

        (btnBack.drawable as? VectorDrawableCompat)?.mutate()?.setTint(textColors[1])
        (btnBack.drawable as? VectorDrawable)?.mutate()?.setTint(textColors[1])

        appBar.setColors(colors)

        mToolbarHelper?.updateTextColors(textColors)

        view?.let { makeCardView(it, cardWithInfo) }
    }

    private var selectedToolbarItem: ToolbarItem? = null

    private fun onSelect(toolbarItem: ToolbarItem) {
        if (isEnableBack) return
            isEnableBack = true
        btnBack.postDelayed({
            isEnableBack = false
        }, 1000)
        if (toolbarItem != selectedToolbarItem) {
            selectedToolbarItem = toolbarItem
            val fragment = when (toolbarItem.section) {
                ToolbarItem.Section.CARD_CONTROL -> CardControlFragment.newInstance()
                ToolbarItem.Section.CARD_STATEMENT -> CardStatementFragment.newInstance()
                ToolbarItem.Section.CARD_INFO -> CardInfoFragment.newInstance()
                else -> throw IllegalArgumentException("Unknown toolbar item ${toolbarItem.section}")
            }
            showFragment(fragment)
        }
    }

    private fun showFragment(fragment: BaseFragment) {
        childFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out)
                .replace(R.id.flCardHost, fragment)
                .commit()
    }

}