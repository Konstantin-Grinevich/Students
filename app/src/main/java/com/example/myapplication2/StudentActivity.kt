package com.example.myapplication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication2.data.Faculty
import com.example.myapplication2.data.Student
import java.time.LocalDate
import java.time.format.DateTimeParseException
import kotlin.streams.toList

private const val ERROR_MESSAGE = "Students list is null"
private const val STUDENTS = "Students"
private const val CHANGE = "Change"
private const val INSERT = "Insert"
private const val CURRENT_INDEX = "Index"
private const val CHECK = "Check"
private const val FILL_FIELD_MESSAGE = "Поле должно быть заполнено"
private const val INVALID_DATE = "Неверный формат даты. Необходимый формат: YYYY-MM-DD"
private const val FACULTY = "Faculty"

class StudentActivity : AppCompatActivity() {

    private val facultyViewModel: FacultyViewModel by lazy {
        val provider = ViewModelProvider(this)
        provider[FacultyViewModel::class.java]
    }

    private lateinit var saveStudent: Button
    private lateinit var lnameText: EditText
    private lateinit var nameText: EditText
    private lateinit var surnameText: EditText
    private lateinit var birthdateText: EditText
    private lateinit var facultyText: EditText
    private lateinit var groupText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        saveStudent = findViewById(R.id.btn_save)
        lnameText = findViewById(R.id.lname_inp)
        nameText = findViewById(R.id.name_inp)
        surnameText = findViewById(R.id.surname_inp)
        birthdateText = findViewById(R.id.birthdate_inp)
        facultyText = findViewById(R.id.fac_inp)
        groupText = findViewById(R.id.group_inp)

        val studentList =
            intent?.getParcelableArrayListExtra<Student>(STUDENTS) ?: throw IllegalStateException(
                ERROR_MESSAGE
            )
        val facultyList =
            intent?.getParcelableArrayListExtra<Faculty>(FACULTY) ?: throw IllegalStateException(
                ERROR_MESSAGE
            )

        val change = intent.getBooleanExtra(CHANGE, false)
        val insert = intent.getBooleanExtra(INSERT, false)
        if (change) {
            val currentIndex = intent.getIntExtra(CURRENT_INDEX, -1)
            fillEditTexts(studentList[currentIndex])
        }

        saveStudent.setOnClickListener {
            if (validateFields()) {
                val currentIndex = intent.getIntExtra(CURRENT_INDEX, -1)
                val isChanged =
                    if (currentIndex != -1) checkChangeFields(studentList[currentIndex]) else false
                if (isChanged && change) {
                    val oldFac = studentList[currentIndex].faculty
                    val fac = Faculty(facultyText.text.toString())
                    if (fac.name != studentList[currentIndex].faculty.name && !facultyList.contains(fac)) {
                        facultyList.add(Faculty(facultyText.text.toString()))
                    }
                    studentList[currentIndex] = changeStudent(studentList[currentIndex])
                    val studFac = studentList.stream()
                        .map { s -> s.faculty }
                        .toList()
                    if (!studFac.contains(oldFac)) {
                        facultyList.remove(oldFac)
                    }
                }
                if (insert) {
                    val fac = Faculty(facultyText.text.toString())
                    val student = Student(
                        lnameText.text.toString(),
                        nameText.text.toString(),
                        surnameText.text.toString(),
                        birthdateText.text.toString(),
                        fac,
                        groupText.text.toString()
                    )
                    if (!facultyList.contains(fac)) {
                        facultyList.add(fac)
                    }
                    studentList.add(student)
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(STUDENTS, studentList)
                intent.putExtra(FACULTY, facultyList)
                intent.putExtra(CHECK, true)
                startActivity(intent)
            }
        }
    }

    private fun fillEditTexts(student: Student) {
        lnameText.append(student.lastName)
        nameText.append(student.name)
        surnameText.append(student.surName)
        birthdateText.append(student.birthDate)
        facultyText.append(student.faculty.name)
        groupText.append(student.group)
    }

    private fun validateFields() : Boolean {
        var isValidate = true
        val currentFac = Faculty(facultyText.text.toString())
        when {
            lnameText.text.toString().isBlank() -> {
                lnameText.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            nameText.text.toString().isBlank() -> {
                nameText.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            surnameText.text.toString().isBlank() -> {
                surnameText.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            facultyText.text.toString().isBlank() -> {
                facultyText.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            groupText.text.toString().isBlank() -> {
                groupText.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
        }
        try {
            val localDate = LocalDate.parse(birthdateText.text.toString())
        } catch(ex : DateTimeParseException) {
            birthdateText.error = INVALID_DATE
            isValidate = false
        }
        return isValidate
    }

    private fun checkChangeFields(student: Student): Boolean {
        when {
            lnameText.text.toString() != student.lastName -> return true
            nameText.text.toString() != student.name -> return true
            surnameText.text.toString() != student.surName -> return true
            birthdateText.text.toString() != student.birthDate -> return true
            facultyText.text.toString() != student.faculty.name -> return true
            groupText.text.toString() != student.group -> return true
        }
        return false
    }

    private fun changeStudent(student:Student) : Student {
        student.lastName = lnameText.text.toString()
        student.name = nameText.text.toString()
        student.surName = surnameText.text.toString()
        student.birthDate = birthdateText.text.toString()
        student.faculty = Faculty(facultyText.text.toString())
        student.group = groupText.text.toString()
        return student
    }
}