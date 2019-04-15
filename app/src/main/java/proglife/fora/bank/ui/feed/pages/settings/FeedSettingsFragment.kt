package proglife.fora.bank.ui.feed.pages.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_feed_settings.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.widgets.TopDividerDecoration

/**
 * Created by Evhenyi Shcherbyna on 07.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FeedSettingsFragment: BaseFragment(), FeedSettingsView {

    companion object {
        fun newInstance(): Fragment {
            return FeedSettingsFragment()
        }
    }

    lateinit var adapter : FeedSettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FeedSettingsAdapter(FeedSettingsAdapter.Settings.authUser())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvSettings.adapter = adapter
        rvSettings.addItemDecoration(TopDividerDecoration(ContextCompat.getDrawable(context!!, R.drawable.drawable_divider)!!))
    }

}