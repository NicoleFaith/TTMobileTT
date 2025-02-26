package com.example.ttanader.LogSign

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ttanader.R

class OpeningPage : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private var progressStatus = 0
    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_opening_page)

        progressBar = findViewById(R.id.progressBar2)
        progressText = findViewById(R.id.progressText) // Find TextView
        progressBar.max = 100 // Set max value

        // Simulating progress over 5 seconds
        Thread {
            while (progressStatus < 100) {
                progressStatus += 1 // Increase progress
                handler.post {
                    progressBar.progress = progressStatus
                    progressText.text = "$progressStatus%" // Update percentage text
                }
                Thread.sleep(30) // 50ms * 100 = 5 seconds total
            }

            // After loading completes, navigate to another activity
            handler.post {
                val intent = Intent(this@OpeningPage, LoginPage::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}
