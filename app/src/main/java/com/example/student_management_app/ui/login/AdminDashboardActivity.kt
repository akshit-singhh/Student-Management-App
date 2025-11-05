package com.example.student_management_app.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.student_management_app.R
import com.example.student_management_app.databinding.ActivityAdminDashboardBinding
import com.example.student_management_app.data.network.ApiClient
import com.example.student_management_app.network.ApiService
import com.example.student_management_app.ui.studentlist.StudentListActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var api: ApiService
    private val handler = Handler(Looper.getMainLooper())
    private val refreshInterval = 15000L //  15 seconds refresh interval

    private val refreshTask = object : Runnable {
        override fun run() {
            loadStudentCount()
            handler.postDelayed(this, refreshInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = ApiClient.apiService

        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val name = prefs.getString("user_name", "Admin")
        binding.tvWelcome.text = "👋 Welcome, $name"

        // Load total students count initially
        loadStudentCount()
        handler.postDelayed(refreshTask, refreshInterval)

        // View students
        binding.btnViewStudents.setOnClickListener {
            startActivity(Intent(this, StudentListActivity::class.java))
        }

        // Update Profile
        binding.btnUpdateProfile.setOnClickListener {
            startActivity(Intent(this, AdminProfileActivity::class.java))
        }

        // Logout
        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun loadStudentCount() {
        binding.lottieStudentLoading.visibility = View.VISIBLE
        binding.lottieStudentLoading.playAnimation()
        binding.tvStudentCount.visibility = View.INVISIBLE

        api.getStudentCount().enqueue(object : Callback<Map<String, Int>> {
            override fun onResponse(call: Call<Map<String, Int>>, response: Response<Map<String, Int>>) {
                binding.lottieStudentLoading.pauseAnimation()
                binding.lottieStudentLoading.visibility = View.GONE
                binding.tvStudentCount.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val count = response.body()?.get("total_students") ?: 0
                    binding.tvStudentCount.text = "$count"
                } else {
                    binding.tvStudentCount.text = "N/A"
                }
            }

            override fun onFailure(call: Call<Map<String, Int>>, t: Throwable) {
                binding.lottieStudentLoading.pauseAnimation()
                binding.lottieStudentLoading.visibility = View.GONE
                binding.tvStudentCount.visibility = View.VISIBLE
                binding.tvStudentCount.text = "Error"
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(refreshTask) // Stop auto-refresh when leaving
    }

    private fun showLogoutDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_logout, null)

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .create()

        val btnLogoutConfirm = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogoutConfirm)
        val btnCancelLogout = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCancelLogout)
        btnLogoutConfirm.setOnClickListener {
            handler.removeCallbacks(refreshTask)
            val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            dialog.dismiss()
        }
        btnCancelLogout.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
