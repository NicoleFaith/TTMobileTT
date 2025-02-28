package com.example.ttanader.models

data class Task(
    val name: String,
    val status: String = "Ongoing", // Default status if not provided
    val assignedMember: String = "" // Default empty string for assigned member
)
