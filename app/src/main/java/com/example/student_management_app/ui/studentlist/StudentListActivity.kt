package com.example.student_management_app.ui.studentlist

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.student_management_app.R
import com.example.student_management_app.data.network.ApiClient
import com.example.student_management_app.databinding.ActivityStudentListBinding
import com.example.student_management_app.data.model.Student
import com.example.student_management_app.network.ApiService
import com.example.student_management_app.ui.adapter.StudentAdapter
import com.example.student_management_app.ui.studentform.AddEditStudentActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentListBinding
    private lateinit var adapter: StudentAdapter
    private lateinit var api: ApiService
    private val studentList = mutableListOf<Student>()
    private var rotateAnimator: ObjectAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = ApiClient.apiService

        // Back button click
        binding.btnBack.setOnClickListener {
            finish()
        }

        setupRecyclerView()
        fetchStudents()

        // FAB to add student
        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditStudentActivity::class.java))
        }

        // Refresh button with rotation animation
        binding.btnRefresh.setOnClickListener {
            startRotateAnimation()
            fetchStudents {
                stopRotateAnimation()
                Toast.makeText(this, "List refreshed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the student list when returning from Add/Edit screen
        fetchStudents()
    }

    private fun setupRecyclerView() {
        adapter = StudentAdapter(
            students = studentList,
            onEdit = { student ->
                val intent = Intent(this, AddEditStudentActivity::class.java).apply {
                    putExtra("id", student.id)
                    putExtra("name", student.name)
                    putExtra("className", student.class_name)
                    putExtra("rollNo", student.roll_no)
                    putExtra("contact", student.contact)
                }
                startActivity(intent)
            },
            onDelete = { student ->
                showDeleteConfirmationDialog(student)
            }
        )

        binding.rvStudents.layoutManager = LinearLayoutManager(this)
        binding.rvStudents.adapter = adapter
    }

    // Show custom confirmation dialog before deleting a student
    private fun showDeleteConfirmationDialog(student: Student) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_delete_confirmation, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)

        tvMessage.text = "Are you sure you want to delete ${student.name}?"
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnDelete.setOnClickListener {
            dialog.dismiss()
            deleteStudent(student) // call your existing delete logic
        }
        dialog.show()
    }
    // API call to delete a student
    private fun deleteStudent(student: Student) {
        api.deleteStudent(student.id ?: return).enqueue(object : Callback<Map<String, Boolean>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<Map<String, Boolean>>,
                response: Response<Map<String, Boolean>>
            ) {
                if (response.isSuccessful) {
                    val deleted = response.body()?.get("deleted") ?: true
                    if (deleted) {
                        studentList.remove(student)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(
                            this@StudentListActivity,
                            "${student.name} deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@StudentListActivity,
                            "Failed to delete ${student.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@StudentListActivity,
                        "Failed to delete ${student.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                Toast.makeText(
                    this@StudentListActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // Fetch students list
    private fun fetchStudents(onComplete: (() -> Unit)? = null) {
        api.getStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                if (response.isSuccessful && response.body() != null) {
                    studentList.clear()
                    studentList.addAll(response.body()!!)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@StudentListActivity,
                        "Failed to load students",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                onComplete?.invoke()
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Toast.makeText(
                    this@StudentListActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                onComplete?.invoke()
            }
        })
    }

    // Rotate animation for refresh icon
    private fun startRotateAnimation() {
        rotateAnimator =
            ObjectAnimator.ofFloat(binding.ivRefreshIcon, "rotation", 0f, 360f).apply {
                duration = 1000
                interpolator = LinearInterpolator()
                repeatCount = ObjectAnimator.INFINITE
                start()
            }
    }

    private fun stopRotateAnimation() {
        rotateAnimator?.cancel()
        binding.ivRefreshIcon.rotation = 0f
    }
}
