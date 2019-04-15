package proglife.fora.bank.ui.finances.bonds

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
import kotlinx.android.synthetic.main.fragment_bonds.*
import proglife.fora.bank.R
import proglife.fora.bank.features.bonds.models.Bond
import proglife.fora.bank.ui.base.BaseFragment

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class BondsFragment: BaseFragment(), BondsView {

    companion object {
        fun newInstance(): BondsFragment = BondsFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: BondsPresenter
    private lateinit var mAdapter: BondsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = BondsAdapter(object : BondsAdapter.OnBondActionListener {
            override fun onSearchTyped(text: String) {
                mPresenter.searchBond(text)
            }

            override fun onBuyBond() {
                mPresenter.buyBond()
            }

            override fun onBondSelect(bond: Bond) {
                mPresenter.selectBond(bond)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bonds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dividerDrawable: Drawable = ContextCompat.getDrawable(context!!, R.drawable.drawable_divider)!!
        rvItems.layoutManager = LinearLayoutManager(context)
        rvItems.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                val childCount = parent.childCount
                for (i in 1 until childCount - 1) {
                    val child: View = parent.getChildAt(i)
                    val params: RecyclerView.LayoutParams = child.layoutParams as RecyclerView.LayoutParams
                    val top = child.top + params.topMargin
                    val bottom = top + dividerDrawable.intrinsicHeight
                    dividerDrawable.setBounds(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
                    dividerDrawable.draw(c)
                }
            }
        })
        rvItems.adapter = mAdapter
    }

    override fun showBonds(list: List<Bond>) {
        mAdapter.setItems(list)
        mAdapter.notifyDataSetChanged()
    }

}