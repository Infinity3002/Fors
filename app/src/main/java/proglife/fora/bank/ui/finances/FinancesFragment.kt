package proglife.fora.bank.ui.finances

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_finances.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.finances.bills.BillsFragment
import proglife.fora.bank.ui.finances.bonds.BondsFragment
import proglife.fora.bank.ui.finances.cards.list.CardsFragment
import proglife.fora.bank.ui.finances.cells.CellsFragment
import proglife.fora.bank.ui.finances.history.FinanceHistoryFragment
import proglife.fora.bank.ui.finances.statistic.FinanceStatisticFragment
import proglife.fora.bank.utils.OnBackPressedListener
import proglife.fora.bank.widgets.ToolbarHelper
import proglife.fora.bank.widgets.arctabs.ToolbarItem

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FinancesFragment: BaseFragment(), FinancesView, OnBackPressedListener {

    companion object {
        fun newInstance() = FinancesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_finances, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ToolbarHelper(
                rvToolbar,
                listOf(
                        ToolbarItem(ToolbarItem.Section.FINANCES_CARDS, "Карты"),
                        ToolbarItem(ToolbarItem.Section.FINANCES_BILLS, "Счета "),
                        ToolbarItem(ToolbarItem.Section.FINANCES_BILLS, "Кредиты "),
                        ToolbarItem(ToolbarItem.Section.FINANCES_BONDS, "Облигации"),
                        ToolbarItem(ToolbarItem.Section.FINANCES_CELLS, "Ячейки"),
                        ToolbarItem(ToolbarItem.Section.FINANCES_HISTORY, "История"),
                        ToolbarItem(ToolbarItem.Section.FINANCES_STATISTIC, "Статистика")
                ),
                selectListener = this::onSelect
        )
        bottomBarState(false)
    }

    private var selectedToolbarItem: ToolbarItem? = null

    private fun onSelect(toolbarItem: ToolbarItem) {
        if (toolbarItem != selectedToolbarItem) {
            selectedToolbarItem = toolbarItem
            val fragment: BaseFragment = when (toolbarItem.section) {
                ToolbarItem.Section.FINANCES_CARDS -> CardsFragment.newInstance()
                ToolbarItem.Section.FINANCES_BILLS -> BillsFragment.newInstance()
                ToolbarItem.Section.FINANCES_BONDS -> BondsFragment.newInstance()
                ToolbarItem.Section.FINANCES_CELLS -> CellsFragment.newInstance()
                ToolbarItem.Section.FINANCES_HISTORY -> FinanceHistoryFragment.newInstance()
                ToolbarItem.Section.FINANCES_STATISTIC -> FinanceStatisticFragment.newInstance()
                else -> throw IllegalArgumentException("Unknown toolbar item ${toolbarItem.section}")
            }
            showFragment(fragment)
        }
    }

    private fun showFragment(fragment: BaseFragment) {
        val transition = childFragmentManager.beginTransaction()
        if (fragment is CardsFragment) {
        } else {
            transition.setCustomAnimations(R.anim.bottom_in, R.anim.bottom_out)
        }
            transition
                .replace(R.id.flHost, fragment)
                .commit()
    }

    fun getCardViewForTransition(): View? {
        return (childFragmentManager.findFragmentById(R.id.flHost) as? CardsFragment)?.getCardViewForTransition()
    }

    override fun onBackPressed(): Boolean {
        return (childFragmentManager.findFragmentById(R.id.flHost) as? OnBackPressedListener)?.onBackPressed() ?: false
    }
}