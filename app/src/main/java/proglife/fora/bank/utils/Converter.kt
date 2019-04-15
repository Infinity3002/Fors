package proglife.fora.bank.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
fun toMoney(value: Double): String {
    val dfs = DecimalFormatSymbols()
    dfs.decimalSeparator = '.'
    dfs.groupingSeparator = ' '
    val df = DecimalFormat("###,###,###.##", dfs)
    return "${df.format(value)} \u20BD"
}