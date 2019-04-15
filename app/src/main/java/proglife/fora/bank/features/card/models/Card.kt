package proglife.fora.bank.features.card.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Evhenyi Shcherbyna on 19.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Card(
        val id: Long,
        val name: String,
        val balance: Double,
        val number: String,
        val expireMonth: String,
        val expireYear: String,
        val locked: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt() == 1)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeDouble(balance)
        parcel.writeString(number)
        parcel.writeString(expireMonth)
        parcel.writeString(expireYear)
        parcel.writeInt(if (locked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }

}