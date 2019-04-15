package proglife.fora.bank.ui.payments

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_payments.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.widgets.TopDividerDecoration

class PaymentsFragment: BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPayments.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_in))

        rvPayments.adapter = PaymentsAdapter()
        rvPayments.addItemDecoration(TopDividerDecoration(ContextCompat.getDrawable(activity!!, R.drawable.drawable_divider)!!))
    }

}