package com.example.ttanader.models

data class Task(
    val name: String,
    val status: String = "Ongoing",
    val assignedMember: String = ""
)
