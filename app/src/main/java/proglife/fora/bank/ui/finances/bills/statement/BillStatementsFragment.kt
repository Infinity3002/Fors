package proglife.fora.bank.ui.finances.bills.statement

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
import kotlinx.android.synthetic.main.fragment_bill_statements.*
import proglife.fora.bank.R
import proglife.fora.bank.models.Statement
import proglife.fora.bank.ui.base.BaseFragment

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BillStatementsFragment : BaseFragment(), BillStatementsView {

    companion object {
        fun newInstance(): BillStatementsFragment = BillStatementsFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: BillStatementsPresenter
    private lateinit var mAdapter: BillStatementsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = BillStatementsAdapter(object : BillStatementsAdapter.OnStatementActionListener {
            override fun onSearch(text: String) {
            }

            override fun onSelect(statement: Statement) {
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bill_statements, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvItems.layoutManager = LinearLayoutManager(context)
        val dividerDrawable: Drawable = ContextCompat.getDrawable(context!!, R.drawable.drawable_divider)!!
        rvItems.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                c.save()
                val childCount = parent.childCount
                for (i in 0 until childCount) {
                    val child: View = parent.getChildAt(i)
                    if (parent.getChildAdapterPosition(child) < 2) continue
                    val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
                    val top = child.top + params.topMargin
                    val bottom = top + dividerDrawable.intrinsicHeight
                    dividerDrawable.setBounds(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
                    dividerDrawable.draw(c)
                }
                c.restore()
            }
        })
        rvItems.adapter = mAdapter
    }

    override fun showItems(list: List<Any>) {
        mAdapter.setItems(list)
        mAdapter.notifyDataSetChanged()
    }
}