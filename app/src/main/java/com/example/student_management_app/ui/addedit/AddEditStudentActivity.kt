package com.example.student_management_app.ui.studentform

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.student_management_app.databinding.ActivityAddEditStudentBinding
import com.example.student_management_app.data.model.Student
import com.example.student_management_app.data.network.ApiClient
import com.example.student_management_app.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddEditStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditStudentBinding
    private lateinit var api: ApiService
    private var studentId: Int? = null  // null = Add mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize API service
        api = ApiClient.apiService

        // Check if we're editing an existing student
        studentId = intent.getIntExtra("id", -1).takeIf { it != -1 }

        if (studentId != null) {
            // Edit mode
            binding.btnSave.text = "Update Student"
            binding.tvTitle.text = "Edit Student"

            // Prefill data
            binding.etName.setText(intent.getStringExtra("name"))
            binding.etClass.setText(intent.getStringExtra("className"))
            binding.etRollNo.setText(intent.getStringExtra("rollNo"))
            binding.etContact.setText(intent.getStringExtra("contact"))
        } else {
            // Add mode
            binding.btnSave.text = "Save Student"
            binding.tvTitle.text = "Add Student"
        }

        // Handle back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Save / Update student on button click
        binding.btnSave.setOnClickListener {
            try {
                val name = binding.etName.text.toString().trim()
                val className = binding.etClass.text.toString().trim()
                val rollNo = binding.etRollNo.text.toString().trim()
                val contact = binding.etContact.text.toString().trim()

                if (name.isEmpty() || className.isEmpty() || rollNo.isEmpty() || contact.isEmpty()) {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val student = Student(
                    id = studentId,
                    name = name,
                    class_name = className,
                    roll_no = rollNo,
                    contact = contact
                )

                if (studentId == null) addStudent(student) else updateStudent(student)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


     // Add a new student to the database via API
    private fun addStudent(student: Student) {
        api.addStudent(student).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddEditStudentActivity, "Student Added Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddEditStudentActivity, "Failed to add student", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(this@AddEditStudentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


     // Update an existing student via API
    private fun updateStudent(student: Student) {
        // student.id is not null in edit mode
        api.updateStudent(student.id, student).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddEditStudentActivity, "Student Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddEditStudentActivity, "Failed to update student", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(this@AddEditStudentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
