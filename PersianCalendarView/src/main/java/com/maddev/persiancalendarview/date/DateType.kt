package com.maddev.persiancalendarview.date

import android.os.Parcel
import android.os.Parcelable

enum class DateType : Parcelable {
    PERSIAN {
        override fun getInstance(timeInMillis: Long) = PersianDate(timeInMillis)
    },
    GREGORIAN {
        override fun getInstance(timeInMillis: Long) = GregorianDate(timeInMillis)
    };

    abstract fun getInstance(timeInMillis: Long = System.currentTimeMillis()): AbstractDate

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(values().indexOf(this))
    }

    companion object CREATOR : Parcelable.Creator<DateType> {
        override fun createFromParcel(parcel: Parcel): DateType {
            return values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<DateType?> {
            return arrayOfNulls(size)
        }
    }


}