package proglife.fora.bank.ui.finances.statistic.chart

import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * Created by Evhenyi Shcherbyna on 18.07.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */

data class Bubble(
        val title: String,
        val amount: Double,
        val iconUrl: String,
        val color: Int,
        val bubbles: List<Bubble>? = null) : Parcelable {

    var factor: Float = 1f
    var rect: RectF = RectF()
    var translationX: Float = 0f
    var translationY: Float = 0f
    var alpha: Float = 1f

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readInt(),
            parcel.createTypedArrayList(CREATOR)) {
        factor = parcel.readFloat()
        rect = parcel.readParcelable(RectF::class.java.classLoader)
        translationX = parcel.readFloat()
        translationY = parcel.readFloat()
        alpha = parcel.readFloat()
    }

    fun getAmountString(): String {
        val dfs = DecimalFormatSymbols()
        dfs.decimalSeparator = '.'
        dfs.groupingSeparator = ' '
        val df = DecimalFormat("###,###,###.##", dfs)
        return "${df.format(amount)} \u20BD"
    }

    fun width(): Float = rect.width()
    fun height(): Float = rect.height()
    fun radius(): Float = rect.width() / 2
    fun cx(): Float = rect.centerX()
    fun cy(): Float = rect.centerY()
    fun contains(x: Float, y: Float): Boolean = rect.contains(x, y)

    override fun hashCode(): Int {
        return "$title$amount$iconUrl$color".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return hashCode() == other?.hashCode()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeDouble(amount)
        parcel.writeString(iconUrl)
        parcel.writeInt(color)
        parcel.writeTypedList(bubbles)
        parcel.writeFloat(factor)
        parcel.writeParcelable(rect, flags)
        parcel.writeFloat(translationX)
        parcel.writeFloat(translationY)
        parcel.writeFloat(alpha)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Bubble> {
        override fun createFromParcel(parcel: Parcel): Bubble {
            return Bubble(parcel)
        }

        override fun newArray(size: Int): Array<Bubble?> {
            return arrayOfNulls(size)
        }
    }
}