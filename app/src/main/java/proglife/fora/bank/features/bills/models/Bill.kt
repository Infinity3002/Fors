package proglife.fora.bank.features.bills.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Evhenyi Shcherbyna on 02.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Bill(
        val name: String,
        val number: String,
        val balance: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(number)
        parcel.writeDouble(balance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Bill> {
        override fun createFromParcel(parcel: Parcel): Bill {
            return Bill(parcel)
        }

        override fun newArray(size: Int): Array<Bill?> {
            return arrayOfNulls(size)
        }
    }
}