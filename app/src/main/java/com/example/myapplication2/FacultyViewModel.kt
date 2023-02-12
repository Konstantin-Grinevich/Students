package com.example.myapplication2

import androidx.lifecycle.ViewModel
import com.example.myapplication2.data.Faculty
import com.example.myapplication2.data.Student

class FacultyViewModel: ViewModel() {

    var facultyBank = arrayListOf<Faculty>(
        Faculty("Все факультеты"),
        Faculty("ФКТиПМ"),
        Faculty("ФИСМО")
    )

    var currentIndex = 0

    val currentFaculty: String
        get() = facultyBank[currentIndex].name

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % facultyBank.size
    }

    fun moveToPrev() {
        currentIndex = (facultyBank.size+currentIndex - 1) % facultyBank.size
    }

    fun deleteFaculty() {
        facultyBank.removeAt(currentIndex)
        moveToPrev()
    }
}