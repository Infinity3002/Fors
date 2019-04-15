package proglife.fora.bank.ui.feed.pages.offers

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_feed_offers.*
import proglife.fora.bank.R
import proglife.fora.bank.models.NewsItem
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.feed.pages.FeedItemAdapter
import proglife.fora.bank.utils.ItemOverDecoration


/**
 * Created by Evhenyi Shcherbyna on 07.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FeedOffersFragment: BaseFragment(), FeedOffersView {

    companion object {
        fun newInstance(): FeedOffersFragment {
            return FeedOffersFragment()
        }
    }

    lateinit var adapter: FeedItemAdapter
    lateinit var snapHelper: PagerSnapHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = FeedItemAdapter(object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem.buttonText == newItem.buttonText
            }
        })
        snapHelper = PagerSnapHelper()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvOffers.layoutManager = LinearLayoutManager(context!!)
        rvOffers.addItemDecoration(ItemOverDecoration(context!!))
        rvOffers.adapter = adapter

        snapHelper.attachToRecyclerView(rvOffers)

        val server = "http://my.proglife.com.ua/resources/offers_"
        adapter.submitList(arrayListOf(
                NewsItem("Показать магазины на карте", "Скидка 15% клиентам банка при покупке офощей и фруктов в \'Перекрестке\'", "${server}1.png"),
                NewsItem("Подробное сравнение", "Скидка 15% клиентам банка при покупке офощей и фруктов в \'Перекрестке\'","${server}2.png"),
                NewsItem("Перейти на сайт", "Скидка 15% клиентам банка при покупке офощей и фруктов в \'Перекрестке\'","${server}3.png"),
                NewsItem("Оформить сейчас", "Скидка 15% клиентам банка при покупке офощей и фруктов в \'Перекрестке\'","${server}4.png")
        ))
    }

}