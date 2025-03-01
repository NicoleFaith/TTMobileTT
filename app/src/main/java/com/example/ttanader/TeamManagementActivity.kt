package com.example.ttanader

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TeamManagementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_management)

        val btnManageMembers = findViewById<Button>(R.id.btnManageMembers)
        btnManageMembers.setOnClickListener {
            val intent = Intent(this, ManageMembersActivity::class.java)
            startActivity(intent)
        }
    }
}
