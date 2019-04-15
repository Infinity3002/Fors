package proglife.fora.bank.features.card.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Evhenyi Shcherbyna on 19.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class CardWithInfo(
        val card: Card,
        val brand: CardBrand?,
        val bank: CardBank?
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Card::class.java.classLoader),
            parcel.readSerializable() as CardBrand?,
            parcel.readParcelable(CardBank::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(card, flags)
        parcel.writeSerializable(brand)
        parcel.writeParcelable(bank, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CardWithInfo> {
        override fun createFromParcel(parcel: Parcel): CardWithInfo {
            return CardWithInfo(parcel)
        }

        override fun newArray(size: Int): Array<CardWithInfo?> {
            return arrayOfNulls(size)
        }
    }
}