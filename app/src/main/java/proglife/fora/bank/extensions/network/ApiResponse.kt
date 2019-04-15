package proglife.fora.bank.extensions.network

import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 17.09.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class ApiResponse<T>(
        @SerializedName("result") val result: String,
        @SerializedName("errorMessage") val errorMessage: String,
        @SerializedName("data") val data: T
)