package com.example.myapplication2.data

import android.os.Parcel
import android.os.Parcelable

data class Faculty(var name:String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString() as String) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Faculty> {
        override fun createFromParcel(parcel: Parcel): Faculty {
            return Faculty(parcel)
        }

        override fun newArray(size: Int): Array<Faculty?> {
            return arrayOfNulls(size)
        }
    }
}
