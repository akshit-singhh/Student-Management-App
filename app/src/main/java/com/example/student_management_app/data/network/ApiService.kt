package com.example.student_management_app.network

import com.example.student_management_app.data.model.Admin
import com.example.student_management_app.data.model.AdminUpdateRequest
import com.example.student_management_app.data.model.Student
import com.example.student_management_app.model.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("students")
    fun getStudents(): Call<List<Student>>

    @POST("students")
    fun addStudent(@Body student: Student): Call<Student>

    @PUT("students/{id}")
    fun updateStudent(@Path("id") id: Int?, @Body student: Student): Call<Student>

    @DELETE("students/{id}")
    fun deleteStudent(@Path("id") id: Int): Call<Map<String, Boolean>>

    @GET("students/count")
    fun getStudentCount(): Call<Map<String, Int>>

    @PUT("admins/{admin_id}")
    fun updateAdmin(
        @Path("admin_id") adminId: Int,
        @Body updatedAdmin: AdminUpdateRequest
    ): Call<Admin>

}
