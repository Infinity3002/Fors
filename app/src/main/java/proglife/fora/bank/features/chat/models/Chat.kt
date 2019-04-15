package proglife.fora.bank.features.chat.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Evhenyi Shcherbyna on 25.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class Chat(
        val firstName: String,
        val lastName: String,
        val photoUrl: String,
        val date: String,
        val message: String,
        val unreadCount: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(photoUrl)
        parcel.writeString(date)
        parcel.writeString(message)
        parcel.writeInt(unreadCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Chat> {
        override fun createFromParcel(parcel: Parcel): Chat {
            return Chat(parcel)
        }

        override fun newArray(size: Int): Array<Chat?> {
            return arrayOfNulls(size)
        }
    }
}