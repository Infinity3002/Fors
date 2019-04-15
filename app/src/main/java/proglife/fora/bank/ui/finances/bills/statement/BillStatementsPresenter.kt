package proglife.fora.bank.ui.finances.bills.statement

import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.R
import proglife.fora.bank.features.bills.models.StatementGroup
import proglife.fora.bank.models.Statement
import proglife.fora.bank.ui.base.BasePresenter

/**
 * Created by Evhenyi Shcherbyna on 03.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class BillStatementsPresenter : BasePresenter<BillStatementsView>() {

    init {
        viewState.showItems(listOf(
                StatementGroup("Вчера, 5 сентября", "+560,15 \u20BD"),
                Statement("Азбука вкуса","Супермаркет", R.mipmap.tmp_ic_vkusa,1529157150000L),
                Statement("Азбука вкуса","Супермаркет", R.mipmap.tmp_ic_vkusa,1529157150000L),
                Statement("Азбука вкуса","Супермаркет", R.mipmap.tmp_ic_vkusa,1529157150000L),
                StatementGroup("4 сентября", "+6567,07 \u20BD"),
                Statement("Азбука вкуса","Супермаркет", R.mipmap.tmp_ic_vkusa,1529157150000L),
                Statement("Азбука вкуса","Супермаркет", R.mipmap.tmp_ic_vkusa,1529157150000L),
                Statement("Азбука вкуса","Супермаркет", R.mipmap.tmp_ic_vkusa,1529157150000L)
        ))
    }

}