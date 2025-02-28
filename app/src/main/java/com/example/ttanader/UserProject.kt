package com.example.ttanader

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.models.ProjectList
import com.example.ttanader.models.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserProject : AppCompatActivity() {

    private lateinit var adapter: ProjectListAdapter
    private val projectLists = mutableListOf<ProjectList>()
    private val currentUser = "user123"
    private val isAdmin = true
    private val teamMembers = listOf("Alice", "Bob", "Charlie")

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_project)

        // Set Team Name in Header
        val teamName = intent.getStringExtra("TEAM_NAME") ?: "No Team Name"
        findViewById<TextView>(R.id.tvTeamTitle)?.text = teamName

        setupRecyclerView()

        // Handle "Add List" Button Click
        val btnAddProject = findViewById<FloatingActionButton>(R.id.btnAddProject)

        btnAddProject.setOnClickListener {
            showAddListDialog() // Ensure the function is called when clicked
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvProjectLists)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = ProjectListAdapter(
            lists = projectLists,
            isAdmin = isAdmin,
            currentUser = currentUser,
            teamMembers = teamMembers
        ) { listIndex -> showAddTaskDialog(listIndex) }

        recyclerView?.adapter = adapter
    }

    private fun showAddListDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Project")

        val input = EditText(this).apply {
            hint = "Enter New Project name"
        }
        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val listName = input.text.toString().trim()
            if (listName.isNotEmpty()) {
                projectLists.add(ProjectList(listName, mutableListOf()))
                adapter.notifyDataSetChanged()
            } else {
                showToast("Project name cannot be empty")
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun showAddTaskDialog(listIndex: Int) {
        if (listIndex !in projectLists.indices) {
            showToast("Invalid project list selected")
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Task")

        val input = EditText(this).apply {
            hint = "Enter task name"
        }
        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val taskName = input.text.toString().trim()
            if (taskName.isNotEmpty()) {
                projectLists[listIndex].tasks.add(Task(taskName))
                adapter.notifyItemChanged(listIndex)
            } else {
                showToast("Task name cannot be empty")
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
