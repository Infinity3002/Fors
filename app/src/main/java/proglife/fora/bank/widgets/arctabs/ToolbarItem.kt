package proglife.fora.bank.widgets.arctabs

/**
 * Created by Evhenyi Shcherbyna on 06.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class ToolbarItem(
        val section: Section,
        val title: String = "",
        val icon: Int = 0
) {

    enum class Section {
        NEWS_OFFERS,
        NEWS_INFO,
        NEWS_SETTINGS,
        FEED_CURRENT,
        FEED_UPCOMING,

        CARD_CONTROL,
        CARD_STATEMENT,
        CARD_INFO,

        BILL_CONTROL,
        BILL_STATEMENTS,
        BILL_INFO,

        FINANCES_CARDS,
        FINANCES_BILLS,
        FINANCES_BONDS,
        FINANCES_CELLS,
        FINANCES_HISTORY,
        FINANCES_STATISTIC
    }

}

