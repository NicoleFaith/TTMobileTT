package com.example.ttanader

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.models.ProjectList
import com.example.ttanader.models.Task

class ProjectListActivity : AppCompatActivity() {

    private lateinit var adapter: ProjectListAdapter
    private val projectLists = mutableListOf<ProjectList>()

    private val isAdmin = true // Change this based on actual user role logic
    private val currentUser = "user@example.com" // Change to actual logged-in user
    private val teamMembers = listOf("Alice", "Bob", "Charlie") // Sample members

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_list)

        // Set up RecyclerView for project lists
        val recyclerView = findViewById<RecyclerView>(R.id.rvProjectLists)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = ProjectListAdapter(
            lists = projectLists,
            isAdmin = isAdmin,
            currentUser = currentUser,
            teamMembers = teamMembers,
            onAddTaskClick = { listIndex ->
                if (listIndex in projectLists.indices) {
                    showAddTaskDialog(listIndex)
                } else {
                    Toast.makeText(this, "Invalid project list", Toast.LENGTH_SHORT).show()
                }
            }
        )

        recyclerView.adapter = adapter

        // Handle "Add List" button click
        findViewById<Button>(R.id.btnAddList).setOnClickListener {
            showAddProjectDialog()
        }
    }

    // Show dialog to add a new project list
    private fun showAddProjectDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Project")

        val input = EditText(this)
        input.hint = "Enter New Project Title"
        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val listName = input.text.toString().trim()
            if (listName.isNotEmpty()) {
                val newList = ProjectList(name = listName, tasks = mutableListOf()) // Initialize tasks list
                projectLists.add(newList)
                adapter.notifyItemInserted(projectLists.size - 1)
            } else {
                Toast.makeText(this, "Project name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    // Show dialog to add a task inside a project list
    private fun showAddTaskDialog(listIndex: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Task")

        val input = EditText(this)
        input.hint = "Enter task name"
        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val taskName = input.text.toString().trim()
            if (taskName.isNotEmpty()) {
                if (listIndex in projectLists.indices) {
                    val newTask = Task(name = taskName)
                    projectLists[listIndex].tasks.add(newTask)
                    adapter.notifyItemChanged(listIndex) // Update UI
                } else {
                    Toast.makeText(this, "Invalid project list", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
