package proglife.fora.bank.ui.feed.pages.info

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpDelegate
import kotlinx.android.synthetic.main.fragment_feed_info.*
import kotlinx.android.synthetic.main.fragment_feed_info.view.*
import proglife.fora.bank.R
import proglife.fora.bank.models.NewsItem
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.feed.pages.FeedItemAdapter
import proglife.fora.bank.utils.ItemOverDecoration

/**
 * Created by Evhenyi Shcherbyna on 07.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FeedInfoFragment: BaseFragment(), FeedInfoView {

    companion object {
        fun newInstance(): FeedInfoFragment {
            return FeedInfoFragment()
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
        return inflater.inflate(R.layout.fragment_feed_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvInfo.layoutManager = LinearLayoutManager(context)
        rvInfo.addItemDecoration(ItemOverDecoration(context!!))
        rvInfo.adapter = adapter

        snapHelper.attachToRecyclerView(rvInfo)

        val server = "http://my.proglife.com.ua/resources/info_"
        adapter.submitList(arrayListOf(
                NewsItem("", "","${server}1.png"),
                NewsItem("Занять очередь", "","${server}2.png"),
                NewsItem("Купить валюту", "","${server}3.png")
        ))
    }

}