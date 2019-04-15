package proglife.fora.bank.ui.finances.bills.control

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_bill_control.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BillControlFragment : BaseFragment(), BillControlView {

    companion object {
        fun newInstance(): BillControlFragment = BillControlFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: BillControlPresenter
    private lateinit var mAdapter: BillControlAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = BillControlAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bill_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dividerDrawable: Drawable = ContextCompat.getDrawable(context!!, R.drawable.drawable_divider)!!
        rvItems.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                c.save()
                val childCount = parent.childCount
                for (i in 0 until childCount) {
                    val child: View = parent.getChildAt(i)
                    if (parent.getChildAdapterPosition(child) == 0) continue
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

}