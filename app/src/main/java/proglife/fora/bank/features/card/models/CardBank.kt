package proglife.fora.bank.features.card.models

import android.os.Parcel
import android.os.Parcelable

data class CardBank(
            val name: String,
            val nameEn: String,
            val alias: String,
            val backgroundColors: Set<String>,
            val textColor: String,
            val prefixes: Set<String>
    ): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString().split(DELIMITER).toSet(),
                parcel.readString(),
                parcel.readString().split(DELIMITER).toSet())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(nameEn)
            parcel.writeString(backgroundColors.joinToString(DELIMITER))
            parcel.writeString(alias)
            parcel.writeString(textColor)
            parcel.writeString(prefixes.joinToString(DELIMITER))
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<CardBank> {
            const val DELIMITER = ","

            override fun createFromParcel(parcel: Parcel): CardBank {
                return CardBank(parcel)
            }

            override fun newArray(size: Int): Array<CardBank?> {
                return arrayOfNulls(size)
            }
        }
    }