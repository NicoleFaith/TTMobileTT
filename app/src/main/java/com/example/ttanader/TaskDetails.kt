package com.example.ttanader

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TaskDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        // Get task data from intent
        val taskName = intent.getStringExtra("TASK_NAME") ?: "No Task Name"
        val assignedMember = intent.getStringExtra("ASSIGNED_MEMBER") ?: "Not Assigned"
        val isCompleted = intent.getBooleanExtra("TASK_COMPLETED", false)

        // Set values in UI
        findViewById<TextView>(R.id.tvTaskNameDetails).text = taskName
        findViewById<TextView>(R.id.tvAssignedMember).text = "Assigned: $assignedMember"
        findViewById<TextView>(R.id.tvTaskStatus).text = if (isCompleted) "Status: Completed" else "Status: Pending"
    }
}