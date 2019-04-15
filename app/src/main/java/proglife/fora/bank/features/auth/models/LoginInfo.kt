package proglife.fora.bank.features.auth.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Evhenyi Shcherbyna on 16.09.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class LoginInfo(
        @SerializedName("appId") val appId: String,
        @SerializedName("fingerprint") val fingerprint: Boolean,
        @SerializedName("login") val login: String,
        @SerializedName("password") val password: String,
        @SerializedName("token") val token: String = "",
        @SerializedName("verificationCode") val verificationCode: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appId)
        parcel.writeByte(if (fingerprint) 1 else 0)
        parcel.writeString(login)
        parcel.writeString(password)
        parcel.writeString(token)
        parcel.writeInt(verificationCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginInfo> {
        override fun createFromParcel(parcel: Parcel): LoginInfo {
            return LoginInfo(parcel)
        }

        override fun newArray(size: Int): Array<LoginInfo?> {
            return arrayOfNulls(size)
        }
    }
}