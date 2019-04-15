package proglife.fora.bank.ui.finances.bills.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_bill_detail.*
import proglife.fora.bank.R
import proglife.fora.bank.features.bills.models.Bill
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.finances.bills.control.BillControlFragment
import proglife.fora.bank.ui.finances.bills.info.BillInfoFragment
import proglife.fora.bank.ui.finances.bills.statement.BillStatementsFragment
import proglife.fora.bank.widgets.ToolbarHelper
import proglife.fora.bank.widgets.arctabs.ToolbarItem

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BillDetailFragment : BaseFragment(), BillDetailView {

    companion object {
        private const val BILL = "bill"

        fun newInstance(data: Any): BillDetailFragment {
            val bundle = Bundle()
            bundle.putParcelable(BILL, data as Bill)
            val fragment = BillDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    @InjectPresenter
    lateinit var mPresenter: BillDetailPresenter
    lateinit var mToolbarHelper: ToolbarHelper

    @ProvidePresenter
    fun providePresenter(): BillDetailPresenter = BillDetailPresenter(arguments!!.getParcelable(BILL))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bill_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvInfo.text = "Вклад \"Комфортный\"\nдо 11.01.2018"
        tvBalance.text = "119 556 \u20BD"
        btnBack.setOnClickListener { mPresenter.back() }

        mToolbarHelper = ToolbarHelper(
                rvToolbar,
                listOf(
                        ToolbarItem(ToolbarItem.Section.BILL_CONTROL, getString(R.string.bill_control)),
                        ToolbarItem(ToolbarItem.Section.BILL_STATEMENTS, getString(R.string.bill_statement)),
                        ToolbarItem(ToolbarItem.Section.BILL_INFO, getString(R.string.bill_info))
                ),
                selectListener = this::onSelect
        )

        toolbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val factor = Math.abs(verticalOffset.toFloat() / appBarLayout.totalScrollRange)
            headerContainer.apply {
                scaleX = 0.7f + (1.0f - factor) * 0.3f
                scaleY = 0.7f + (1.0f - factor) * 0.3f
                alpha = 1.0f - factor
            }
        }

        bottomBarState(true)
    }

    private var selectedToolbarItem: ToolbarItem? = null

    private fun onSelect(toolbarItem: ToolbarItem) {
        if (toolbarItem != selectedToolbarItem) {
            selectedToolbarItem = toolbarItem
            val fragment = when (toolbarItem.section) {
                ToolbarItem.Section.BILL_CONTROL -> BillControlFragment.newInstance()
                ToolbarItem.Section.BILL_STATEMENTS -> BillStatementsFragment.newInstance()
                ToolbarItem.Section.BILL_INFO -> BillInfoFragment.newInstance()
                else -> throw IllegalArgumentException("Unknown toolbar item ${toolbarItem.section}")
            }
            showFragment(fragment)
        }
    }

    private fun showFragment(fragment: BaseFragment) {
        fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out)
                .replace(R.id.flHost, fragment)
                .commit()
    }

}