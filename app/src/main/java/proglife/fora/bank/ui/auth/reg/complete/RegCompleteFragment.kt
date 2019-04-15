package proglife.fora.bank.ui.auth.reg.complete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_auth_reg_complete.*
import kotlinx.android.synthetic.main.fragment_auth_reg_complete.view.*
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.R
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.utils.OnBackPressedListener

/**
 * Created by Evhenyi Shcherbyna on 12.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class RegCompleteFragment : BaseFragment(), OnBackPressedListener {

    private val STATE_ONE = intArrayOf(
            R.attr.state_one, -R.attr.state_two, -R.attr.state_three
    )

    private val STATE_TWO = intArrayOf(
            -R.attr.state_one, R.attr.state_two, -R.attr.state_three
    )

    private val STATE_THREE = intArrayOf(
            -R.attr.state_one, -R.attr.state_two, R.attr.state_three
    )

    private var blocked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth_reg_complete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).bottomBarState(true)
        view.animated.setImageState(STATE_ONE, true)
        view.animated.postDelayed({
            view.animated.setImageState(STATE_TWO, true)
        }, resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())

        val delay = resources.getInteger(android.R.integer.config_longAnimTime).toLong()
        tvTitle.animate().alpha(1f).setStartDelay(delay).start()
        tvMessage.animate().alpha(1f).setStartDelay(delay).start()
        btnNext.animate().alpha(1f).setStartDelay(delay).start()

        btnNext.setOnClickListener { complete() }
    }

    private fun complete() {
        if (blocked) return
        blocked = true
        view?.let {
            it.animated.setImageState(STATE_THREE, true)
            tvTitle.animate().alpha(0f).start()
            tvMessage.animate().alpha(0f).start()
            btnNext.animate().alpha(0f).start()
            it.postDelayed({
                (activity as MainActivity).router.newRootScreen(Screens.LOGIN)
            }, resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
        }
    }

    override fun onBackPressed(): Boolean {
        complete()
        return true
    }
}