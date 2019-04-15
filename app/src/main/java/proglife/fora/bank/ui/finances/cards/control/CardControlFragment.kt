package proglife.fora.bank.ui.finances.cards.control

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_card_control.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.widgets.TopDividerDecoration

/**
 * Created by Evhenyi Shcherbyna on 20.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class CardControlFragment: BaseFragment(), CardControlView  {

    companion object {
        fun newInstance() = CardControlFragment()
    }

    private lateinit var adapter: CardControlAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CardControlAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvItems.addItemDecoration(TopDividerDecoration(ContextCompat.getDrawable(activity!!, R.drawable.drawable_divider)!!, 1))
        rvItems.adapter = CardControlAdapter()
    }

}