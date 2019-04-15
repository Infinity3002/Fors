package proglife.fora.bank.ui.finances.cards.list

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_card_list.*
import kotlinx.android.synthetic.main.li_card_for_list.*
import kotlinx.android.synthetic.main.li_card_for_list.view.*
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.R
import proglife.fora.bank.features.card.CardUtils
import proglife.fora.bank.features.card.models.CardWithInfo
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.utils.inflate
import proglife.fora.bank.utils.isVisibly
import proglife.fora.bank.widgets.CardsLayout
import timber.log.Timber

/**
 * Created by Evhenyi Shcherbyna on 13.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CardsFragment : BaseFragment(), CardsView {

    companion object {
        const val CARD_VIEW = "card_view"

        fun newInstance(): CardsFragment = CardsFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: CardsPresenter

    private var selectedView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isEnable = false
        cardsLayout.callback = object : CardsLayout.Callback {
            override fun onMoveToBack(data: Any?) {
                if (data is CardWithInfo) {
                    mPresenter.moveToLast(data)
                }
            }

            override fun onLongPress(view: View) {
                if ((view.tag as CardWithInfo).card.locked == true) return
                cardsLayout.collapse(view)
            }

            override fun onClick(view: View) {
                if ((view.tag as CardWithInfo).card.locked == true) return
                onSelectCardView(view)
                mPresenter.select(view.tag as CardWithInfo)
            }

            override fun onCollapse(view: View) {
                arrayOf(spSort, ivSpSort,  btnAddCard).forEach {
                    it.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            it.visibility = View.GONE
                        }
                    })
                }

                arrayOf(btnRefill, btnTransfer, btnLock, btnCardAllActions).forEach {
                    it.animate().alpha(1f).setListener(object :AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator?) {
                            it.visibility = View.VISIBLE
                        }
                    })
                }

                btnCardAllActions.setOnClickListener {
                    onSelectCardView(view)
                    mPresenter.select(view.tag as CardWithInfo)
                }
                btnLock.setOnClickListener { _ ->
                    onSelectCardView(view)
                    mPresenter.block(view.tag as CardWithInfo)
                }
            }

            override fun onExtend() {
                arrayOf(spSort, ivSpSort, btnAddCard).forEach {
                    it.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator?) {
                            it.visibility = View.VISIBLE
                        }
                    })
                }

                arrayOf(btnRefill, btnTransfer, btnLock, btnCardAllActions).forEach {
                    it.animate().alpha(0f).setListener(object :AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            it.visibility = View.GONE
                        }
                    })
                }
            }
        }

        mPresenter.loadCards()
        (context as MainActivity).bottomBarState(false)
    }

    private var isEnable =false

    private fun onSelectCardView(view: View) {
        if(isEnable) return
        isEnable = true
        selectedView?.let { it.transitionName = null }
        selectedView = view
        selectedView!!.transitionName = CARD_VIEW + (view.tag as CardWithInfo).card.id
    }

    override fun showCardsLoading() {
        pbCards.visibility = View.VISIBLE
    }

    override fun dismissCardsLoading() {
        pbCards.visibility = View.GONE
    }

    override fun showCards(list: List<CardWithInfo>) {
        list.reversed().forEach { cardsLayout.addView(makeCardView(it, cardsLayout)) }
    }

    private fun makeCardView(cardWithInfo: CardWithInfo, container: ViewGroup): View {
        val view = container.inflate(R.layout.li_card_for_list)
        bindCardView(cardWithInfo, view)
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun bindCardView(cardWithInfo: CardWithInfo, view: View) {
        val card = cardWithInfo.card
        val brand = cardWithInfo.brand
        val bank = cardWithInfo.bank

        view.transitionName = CARD_VIEW + card.id
        view.tvNumber.text = card.number
        view.tvDate.text = "${card.expireMonth}/${card.expireYear}"
        view.tvBalance.text = context?.getString(R.string.card_balance, card.balance)
        view.ivLock.isVisibly(card.locked)

        if (bank != null) {
            val gradientColors = bank.backgroundColors.map { Color.parseColor(it) }.toIntArray()
            val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, gradientColors)
            view.ivBackground.setImageDrawable(gradientDrawable)

            try {
                val stream = context!!.assets.open(CardUtils.BANKS_PATH + bank.alias + ".png")
                val drawable = Drawable.createFromStream(stream, bank.alias)
                view.ivLogoBank.setImageDrawable(drawable)
            } catch (e : RuntimeException) {
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
            } catch (e : RuntimeException) {
                Timber.i(e)
            }
        }

        view.tag = cardWithInfo
    }

    fun getCardViewForTransition(): View? = selectedView

    override fun updateCard(card: CardWithInfo) {
        for (i in 0 until cardsLayout.childCount) {
            if ((cardsLayout.getChildAt(i).tag as CardWithInfo).card.id == card.card.id) {
                bindCardView(card, cardsLayout.getChildAt(i))
                break
            }
        }
    }

}