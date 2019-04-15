package proglife.fora.bank.features.bonds.models

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Bond(
        val name: String,
        val info: String,
        val percent: Float,
        val iconUrl: String
)