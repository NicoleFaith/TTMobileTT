package com.example.ttanader.LogSign

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ttanader.R
import com.google.android.material.progressindicator.LinearProgressIndicator

class OpeningPage : AppCompatActivity() {

    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var progressText: TextView
    private var progressStatus = 0
    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // Hide action bar for a cleaner look
        setContentView(R.layout.activity_opening_page)

        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)

        // Simulate loading process with animation
        simulateLoading()
    }

    private fun simulateLoading() {
        val runnable = object : Runnable {
            override fun run() {
                if (progressStatus < 100) {
                    progressStatus += 5
                    progressBar.setProgressCompat(progressStatus, true) // Smooth animation
                    progressText.text = "Loading... $progressStatus%" // Update text or num percentage
                    handler.postDelayed(this, 150) // Update every 150ms
                } else {
                    navigateToLogin()
                }
            }
        }
        handler.post(runnable)
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginPage::class.java))
        finish()
    }
}
