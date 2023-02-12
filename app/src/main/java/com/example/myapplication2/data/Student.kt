package com.example.myapplication2.data

import android.os.Parcel
import android.os.Parcelable

data class Student(var lastName: String, var name:String, var surName: String, var birthDate: String, var faculty: Faculty, var group: String) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readString() as String,
        parcel.readString() as String,
        Faculty(parcel.readString() as String),
        parcel.readString() as String
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lastName)
        parcel.writeString(name)
        parcel.writeString(surName)
        parcel.writeString(birthDate)
        parcel.writeString(faculty.name)
        parcel.writeString(group)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}
