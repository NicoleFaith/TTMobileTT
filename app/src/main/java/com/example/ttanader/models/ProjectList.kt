package com.example.ttanader.models

data class ProjectList(
    val name: String,
    val tasks: MutableList<Task> = mutableListOf() // Ensure tasks list is initialized
)
