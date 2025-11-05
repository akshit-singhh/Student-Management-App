package com.example.student_management_app.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val role: String? = null,
    val admin_id: Int? = null,
    val name: String? = null,
    val email: String? = null
)

data class ApiResponse(
    val success: Boolean,
    val message: String
)
