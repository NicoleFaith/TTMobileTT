package com.example.ttanader.models

data class ProjectList(
    var name: String,
    val tasks: MutableList<Task> = mutableListOf()  // Holds the list of tasks
)
