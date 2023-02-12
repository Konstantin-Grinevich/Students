package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication2.data.Faculty
import com.example.myapplication2.data.Student
import java.lang.Exception
import kotlin.streams.toList

private const val KEY_INDEX = "index"
private const val CHECK = "Check"
private const val STUDENTS = "Students"
private const val ERROR_MESSAGE = "List is null"
private const val STUDENTS_NOT_FOUND = "Нет студентов"
private const val CHANGE = "Change"
private const val INSERT = "Insert"
private const val CURRENT_INDEX = "Index"
private const val ALL_FACULTY = "Все факультеты"
private const val FACULTY = "Faculty"

class MainActivity : AppCompatActivity() {
    private lateinit var addButton: Button
    private lateinit var changeButton: Button
    private lateinit var nextStudButton: Button
    private lateinit var prevStudButton: Button
    private lateinit var nextFacButton: Button
    private lateinit var prevFacButton: Button
    private lateinit var deleteStudButton: Button
    private lateinit var studentFio: TextView
    private lateinit var facultyName: TextView

    private val studentViewModel: StudentViewModel by lazy {
        val provider = ViewModelProvider(this)
        provider[StudentViewModel::class.java]
    }

    private val facultyViewModel: FacultyViewModel by lazy {
        val provider = ViewModelProvider(this)
        provider[FacultyViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        studentViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX)?:0
        facultyViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX)?:0

        addButton = findViewById(R.id.btn_add_stud)
        changeButton = findViewById(R.id.btn_change_stud)
        nextStudButton = findViewById(R.id.btn_next_stud)
        prevStudButton = findViewById(R.id.btn_prev_stud)
        studentFio = findViewById(R.id.fio)
        facultyName = findViewById(R.id.faculty)
        nextFacButton = findViewById(R.id.btn_next_fac)
        prevFacButton = findViewById(R.id.btn_prev_fac)
        deleteStudButton = findViewById(R.id.btn_delete_stud)

        val check = intent.getBooleanExtra(CHECK, false)
        if (check) {
            val listStud = intent?.getParcelableArrayListExtra<Student>(STUDENTS)?: throw IllegalStateException(
                ERROR_MESSAGE)
            val listFac = intent?.getParcelableArrayListExtra<Faculty>(FACULTY)?: throw IllegalStateException(
                ERROR_MESSAGE)
            studentViewModel.studentBank = listStud
            facultyViewModel.facultyBank = listFac
            intent.putExtra(CHECK, false)
        }
        updateFaculty()
        val notExistFac = updateStudent(true)
        if (notExistFac) {
            facultyViewModel.deleteFaculty()
        }
        nextStudButton.setOnClickListener {
            if (studentFio.text != STUDENTS_NOT_FOUND) {
                studentViewModel.moveToNext()
                updateStudent(true)
            }
        }
        prevStudButton.setOnClickListener {
            if (studentFio.text != STUDENTS_NOT_FOUND) {
                studentViewModel.moveToPrev()
                updateStudent(false)
            }
        }

        nextFacButton.setOnClickListener {
            facultyViewModel.moveToNext()
            updateFaculty()
            updateStudent(true)
        }
        prevFacButton.setOnClickListener {
            facultyViewModel.moveToPrev()
            updateFaculty()
            updateStudent(true)
        }

        deleteStudButton.setOnClickListener {
            if (studentFio.text.toString() != STUDENTS_NOT_FOUND) {
                val fac = Faculty(studentViewModel.currentStudentFaculty)
                val isEnd = studentViewModel.deleteStudent()
                val facs = studentViewModel.studentBank.stream()
                    .map { s -> s.faculty }
                    .toList()
                if (isEnd) {
                    studentFio.text = STUDENTS_NOT_FOUND
                    facultyViewModel.facultyBank.removeAll(facultyViewModel.facultyBank)
                    facultyViewModel.facultyBank.add(Faculty(ALL_FACULTY))
                    facultyViewModel.currentIndex = 0
                    updateFaculty()
                    updateStudent(false)
                    return@setOnClickListener
                }
                val facultyNotFound = !facs.contains(fac)
                if (facultyNotFound) {
                    facultyViewModel.facultyBank.remove(fac)
                    updateFaculty()
                    updateStudent(false)
                    return@setOnClickListener
                }
                val isNotExist = updateStudent(true)
                updateFaculty()
                    if (isNotExist) {
                        facultyViewModel.deleteFaculty()
                    }
                updateStudent(false)
            }
        }

        addButton.setOnClickListener {
            val intent = Intent(this, StudentActivity::class.java)
            intent.putExtra(STUDENTS, studentViewModel.studentBank)
            intent.putExtra(FACULTY, facultyViewModel.facultyBank)
            intent.putExtra(CHANGE, false)
            intent.putExtra(INSERT, true)
            startActivity(intent)
        }

        changeButton.setOnClickListener {
            if (studentFio.text.toString() != STUDENTS_NOT_FOUND) {
                val intent = Intent(this, StudentActivity::class.java)
                intent.putExtra(STUDENTS, studentViewModel.studentBank)
                intent.putExtra(FACULTY, facultyViewModel.facultyBank)
                intent.putExtra(CURRENT_INDEX, studentViewModel.currentIndex)
                intent.putExtra(CHANGE, true)
                intent.putExtra(INSERT, false)
                startActivity(intent)
            }
        }
    }

    private fun updateStudent(next:Boolean): Boolean {
        val faculty = facultyName.text.toString()
        val size = studentViewModel.studentBank.size
        var i = 0
        if (faculty != ALL_FACULTY && studentViewModel.studentBank.isNotEmpty()) {
            var facultyStud = studentViewModel.currentStudentFaculty
            while (facultyStud != faculty && i < size ) {
                if (next) {
                    studentViewModel.moveToNext()
                } else {
                    studentViewModel.moveToPrev()
                }
                facultyStud = studentViewModel.currentStudentFaculty
                i++
            }
        }
        val fio = if (i != size) studentViewModel.currentStudentFio else STUDENTS_NOT_FOUND
        studentFio.text = fio
        return fio == STUDENTS_NOT_FOUND
    }

    private fun updateFaculty() {
        try {
            val name = facultyViewModel.currentFaculty
            facultyName.text = name
        } catch (e : Exception) {
            facultyViewModel.moveToPrev()
            updateFaculty()
        }
    }

}