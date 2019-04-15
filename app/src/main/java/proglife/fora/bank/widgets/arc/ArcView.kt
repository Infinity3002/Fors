package proglife.fora.bank.widgets.arc

/**
 * Created by Evhenyi Shcherbyna on 04.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface ArcView {

    companion object {
        const val GRADIENT_DURATION = 1000L
        const val TRANSITION_NAME = "arc_view"
    }

    fun setColors(colors: IntArray, animate: Boolean = false)
    fun getColors(): IntArray

}