package com.example.student_management_app.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.student_management_app.data.model.Admin
import com.example.student_management_app.databinding.ActivityAdminProfileBinding
import com.example.student_management_app.data.model.AdminUpdateRequest
import com.example.student_management_app.data.network.ApiClient
import com.example.student_management_app.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminProfileBinding
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = ApiClient.apiService

        // Load profile data from SharedPreferences
        loadProfileData()

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Update profile
        binding.btnUpdate.setOnClickListener { updateProfile() }
    }

    private fun loadProfileData() {
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val currentName = prefs.getString("user_name", "")
        val currentEmail = prefs.getString("user_email", "")

        binding.etName.setText(currentName)
        binding.etEmail.setText(currentEmail)
    }

    private fun updateProfile() {
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val adminId = prefs.getInt("admin_id", -1)
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (adminId == -1) {
            Toast.makeText(this, "Invalid admin session", Toast.LENGTH_SHORT).show()
            return
        }

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Name and Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedAdmin = AdminUpdateRequest(
            name = name,
            email = email,
            password = if (password.isEmpty()) null else password
        )

        api.updateAdmin(adminId, updatedAdmin).enqueue(object : Callback<Admin> {
            override fun onResponse(
                call: Call<Admin>,
                response: Response<Admin>
            ) {
                if (response.isSuccessful) {
                    val admin = response.body()

                    // Save updated info immediately
                    prefs.edit()
                        .putString("user_name", admin?.name)
                        .putString("user_email", admin?.email)
                        .apply()

                    Toast.makeText(
                        this@AdminProfileActivity,
                        "Profile updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate back to Dashboard and clear this activity
                    val intent = Intent(this@AdminProfileActivity, AdminDashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(
                        this@AdminProfileActivity,
                        "Update failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Admin>, t: Throwable) {
                Toast.makeText(
                    this@AdminProfileActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // reload fields every time the activity resumes
    override fun onResume() {
        super.onResume()
        loadProfileData()
    }
}
