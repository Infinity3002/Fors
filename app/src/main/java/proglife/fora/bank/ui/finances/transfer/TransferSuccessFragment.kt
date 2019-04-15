package proglife.fora.bank.ui.finances.transfer

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_transfer_success.*
import kotlinx.android.synthetic.main.fragment_transfer_success.view.*
import proglife.fora.bank.R
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.utils.OnBackPressedListener

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class TransferSuccessFragment: BaseFragment(), OnBackPressedListener {

    companion object {

        fun newInstance(): TransferSuccessFragment {
            return TransferSuccessFragment()
        }

    }

    private val STATE_ONE = intArrayOf(
            R.attr.state_one, -R.attr.state_two, -R.attr.state_three
    )

    private val STATE_TWO = intArrayOf(
            -R.attr.state_one, R.attr.state_two, -R.attr.state_three
    )

    private val STATE_FOUR = intArrayOf(
            -R.attr.state_one, R.attr.state_two, R.attr.state_three
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transfer_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBarState(true)

        btnBack.setOnClickListener { (context as MainActivity).router.backTo(Screens.FINANCES) }

        tvAmount.text = "-9550 \u20BD"
        tvInfo.text = getString(R.string.transfer_success)

        tvAmount.alpha = 0f
        tvInfo.alpha = 0f
        val delay = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        tvAmount.animate().alpha(1f).setStartDelay(delay).start()
        tvInfo.animate().alpha(1f).setStartDelay(delay).start()

        tvFromName.text = "Зарплатный счет"
        tvFromNumber.text = "4534"
        tvFromAmount.text = "25 100 \u20BD"

        tvToName.text = "Сейф"
        tvToNumber.text = "5134"
        tvToAmount.text = "23 000 \u20BD"

        view.animated.setImageState(STATE_ONE, true)
        view.animated.postDelayed({
            view.animated.setImageState(STATE_TWO, true)
        }, resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())

        sceneLayout.postDelayed({
            view.animated.setImageState(STATE_FOUR, true)
            val transition = AutoTransition()
            transition.interpolator = LinearOutSlowInInterpolator()
            TransitionManager.beginDelayedTransition(sceneLayout, transition)
            phImageTop.setContentId(R.id.animated)
            val constraintSet = ConstraintSet()
            constraintSet.clone(sceneLayout)
            gpBottom.visibility = View.VISIBLE
            constraintSet.setVisibility(R.id.gpBottom, View.VISIBLE)
        }, 1600L)
    }

    override fun onBackPressed(): Boolean {
        (context as MainActivity).router.backTo(Screens.FINANCES)
        return true
    }
}