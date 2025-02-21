package com.example.ttanader.LogSign

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ttanader.R

class SignUpPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page)

        val SelSignUpBtn = findViewById<Button>(R.id.SelSignUpBtn)
        val LoginSignUpPg = findViewById<Button>(R.id.LoginSignUpPg)

        SelSignUpBtn.setOnClickListener {
            val intent = Intent(this, LoginPage ::class.java)
            startActivity(intent)
        }

        LoginSignUpPg.setOnClickListener {
            val intent = Intent(this, LoginPage ::class.java)
            startActivity(intent)
        }

    }
}