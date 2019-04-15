package proglife.fora.bank.ui.finances.bills.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BillInfoFragment : BaseFragment() {

    companion object {
        fun newInstance(): BillInfoFragment = BillInfoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bill_info, container, false)
    }
}