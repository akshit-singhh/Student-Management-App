package com.example.student_management_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.student_management_app.ui.login.LoginActivity
import com.example.student_management_app.ui.studentlist.StudentListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.postDelayed({
            val isLoggedIn = false

            if (isLoggedIn) {
                startActivity(Intent(this, StudentListActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 1500) // 1.5 seconds splash delay
    }
}
