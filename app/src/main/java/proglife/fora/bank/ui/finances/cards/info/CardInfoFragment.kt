package proglife.fora.bank.ui.finances.cards.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment

class CardInfoFragment : BaseFragment() {

    companion object {
        fun newInstance(): CardInfoFragment = CardInfoFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}