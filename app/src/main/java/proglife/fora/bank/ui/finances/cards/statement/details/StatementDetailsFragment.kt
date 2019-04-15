package proglife.fora.bank.ui.finances.cards.statement.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_statement_details.*
import kotlinx.android.synthetic.main.li_statement_details.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.widgets.SwipeListener

class StatementDetailsFragment: BaseFragment(){

    companion object {
        fun newInstance() = StatementDetailsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_statement_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context as MainActivity).bottomBarState(true)
        val adapter = StatementsDetailAdapter()
        rvStatements.adapter = adapter

        sauronLayout.apply {
            alpha = 0f
            scaleY = 0.8f
            animate().alpha(1f).scaleY(1f).setStartDelay(200).setDuration(300)
            contentLayout.post {
                contentLayout.translationX = contentLayout.measuredWidth.toFloat()
                contentLayout.alpha = 1f
                contentLayout.animate().translationX(0f).setStartDelay(300).setDuration(400)
            }
        }
        btnBack.setOnClickListener { activity?.onBackPressed() }
        val collapsedViews = arrayOf(ivLogo, tvTitle, tvDate, tvSum)
        appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val factor = Math.abs(verticalOffset.toFloat() / appBarLayout.totalScrollRange)
            collapsedViews.forEach {
                it.scaleX = 0.7f + (1.0f - factor) * 0.3f
                it.scaleY = 0.7f + (1.0f - factor) * 0.3f
                it.alpha = 1.0f - factor
            }
        }
    }
}