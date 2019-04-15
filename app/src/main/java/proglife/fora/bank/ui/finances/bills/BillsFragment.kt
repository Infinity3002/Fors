package proglife.fora.bank.ui.finances.bills

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_bills.*
import proglife.fora.bank.R
import proglife.fora.bank.features.bills.models.Bill
import proglife.fora.bank.ui.base.BaseFragment

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BillsFragment: BaseFragment(), BillsView {

    companion object {
        fun newInstance(): BillsFragment = BillsFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: BillsPresenter
    private lateinit var mAdapter: BillsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = BillsAdapter(object : BillsAdapter.OnBillActionListener {
            override fun onBillSelect(bill: Bill) {
                mPresenter.selectBill(bill)
            }

            override fun onRefillBill(bill: Bill) {
                mPresenter.refillBill(bill)
            }

            override fun onSendBill(bill: Bill) {
                mPresenter.sendBill(bill)
            }

            override fun onAddBill() {
                mPresenter.addBill()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dividerDrawable: Drawable = ContextCompat.getDrawable(context!!, R.drawable.drawable_divider)!!
        rvItems.layoutManager = LinearLayoutManager(context)
        rvItems.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                val left = parent.paddingLeft + parent.context.resources.getDimensionPixelSize(R.dimen._12sdp)
                val right = parent.width - parent.paddingRight - parent.context.resources.getDimensionPixelSize(R.dimen._12sdp)

                val childCount = parent.childCount
                for (i in 1 until childCount - 1) {
                    val child: View = parent.getChildAt(i)
                    val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
                    val top = child.top + params.topMargin
                    val bottom = top + dividerDrawable.intrinsicHeight
                    dividerDrawable.setBounds(left, top, right, bottom)
                    dividerDrawable.draw(c)
                }
            }
        })
        rvItems.adapter = mAdapter
        bottomBarState(false)
    }

    override fun showBills(list: List<Bill>) {
        mAdapter.setItems(list)
        mAdapter.notifyDataSetChanged()
    }

}