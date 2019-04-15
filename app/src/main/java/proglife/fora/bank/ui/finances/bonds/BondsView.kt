package proglife.fora.bank.ui.finances.bonds

import proglife.fora.bank.features.bonds.models.Bond
import proglife.fora.bank.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface BondsView: BaseView {
    fun showBonds(list: List<Bond>)
}