package com.example.student_management_app.data.model

data class AdminUpdateRequest(
    val name: String,
    val email: String,
    val password: String? = null
)

data class Admin(
    val id: Int,
    val name: String,
    val email: String,
    val password: String
)
