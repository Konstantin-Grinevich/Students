package com.example.myapplication2

import androidx.lifecycle.ViewModel
import com.example.myapplication2.data.Faculty
import com.example.myapplication2.data.Student

class StudentViewModel : ViewModel(){
    var studentBank = arrayListOf<Student>(
        Student("Efimof", "Nik", "Andr", "2001-05-12", Faculty("ФКТиПМ"), "47"),
        Student("Gai", "Chugun", "Rod", "1999-05-03", Faculty("ФИСМО"), "35"),
        Student("Grin", "Konstantin", "Andr", "2001-05-07", Faculty("ФКТиПМ"), "47"),
    )

    var currentIndex = 0

    val currentStudentFio: String
        get() = studentBank[currentIndex].lastName + " " + studentBank[currentIndex].name + " " +
                studentBank[currentIndex].surName

    val currentStudentFaculty: String
        get() = studentBank[currentIndex].faculty.name

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % studentBank.size
    }

    fun moveToPrev() {
        currentIndex = (studentBank.size+currentIndex - 1) % studentBank.size
    }

    fun deleteStudent() : Boolean{
        studentBank.removeAt(currentIndex)
        if (studentBank.size != 0) {
            moveToPrev()
            return false
        }
        return true
    }

}