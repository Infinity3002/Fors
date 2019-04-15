package proglife.fora.bank.ui.feed.pages.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_feed_page.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.feed.FeedHostFragment
import proglife.fora.bank.utils.ItemOverDecoration

/**
 * Created by Evhenyi Shcherbyna on 22.08.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FeedPageFragment : BaseFragment(), FeedPageView {

    companion object {
        private const val FEED_TYPE = "feed_type"

        fun newInstance(feedType: FeedHostFragment.FeedType): Fragment {
            val bundle = Bundle()
            bundle.putSerializable(FEED_TYPE, feedType)
            val fragment = FeedPageFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var mAdapter: FeedItemAdapter
    lateinit var mSnapHelper: PagerSnapHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = when (arguments!!.getSerializable(FEED_TYPE) as FeedHostFragment.FeedType) {
            FeedHostFragment.FeedType.CURRENT -> listOf(
                    R.layout.temp_feed_1,
                    R.layout.temp_feed_2,
                    R.layout.temp_feed_3,
                    R.layout.temp_feed_4,
                    R.layout.temp_feed_5
            )
            FeedHostFragment.FeedType.UPCOMING -> listOf(
                    R.layout.temp_feed_6,
                    R.layout.temp_feed_7,
                    R.layout.temp_feed_8
            )
            else -> emptyList()
        }
        mAdapter = FeedItemAdapter(list)
        mSnapHelper = PagerSnapHelper()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvPages.layoutManager = LinearLayoutManager(context)
        rvPages.addItemDecoration(ItemOverDecoration(context!!))
        rvPages.adapter = mAdapter
        mSnapHelper.attachToRecyclerView(rvPages)
    }



}