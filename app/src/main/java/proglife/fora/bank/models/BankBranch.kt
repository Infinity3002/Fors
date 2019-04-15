package proglife.fora.bank.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 15.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class BankBranch(
    @SerializedName("id") val id: Long,
    @SerializedName("code") val code: Long,
    @SerializedName("name") val name: String,
    @SerializedName("city") val city: String,
    @SerializedName("activity") val activity: String,
    @SerializedName("address") val address: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("schedule") val schedule: String,
    @SerializedName("metro") val metro: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("sort") val sort: Int,
    @SerializedName("change_at") val changeAt: String,
    @SerializedName("phone") val phone: String
)