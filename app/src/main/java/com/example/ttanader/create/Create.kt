package com.example.ttanader.create

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ttanader.AddMembers
import com.example.ttanader.MainActivity
import com.example.ttanader.R
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class Create : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create)

        // Adjust system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Due Date Picker
        val dueDateEditText = findViewById<TextInputEditText>(R.id.dueDateEditText)
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Format the selected date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dueDateEditText.setText(dateFormat.format(calendar.time))
        }

        dueDateEditText.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val NewProjectAddMembersBtn = findViewById<Button>(R.id.NewProjectAddMembersBtn)

        NewProjectAddMembersBtn.setOnClickListener {
            val intent = Intent(this, AddMembers ::class.java)
            startActivity(intent)
        }

    }
}
