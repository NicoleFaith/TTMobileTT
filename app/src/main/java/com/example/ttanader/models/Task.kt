package com.example.ttanader.models

data class Task(
    var name: String,                   // Task name
    var assignedMember: String? = null, // Member assigned to this task (optional)
    var description: String = "",       // Task details (optional)
    var isCompleted: Boolean = false,   // Completion status
    var assignedByAdmin: Boolean = false // Ensures only admin assigns members
)
