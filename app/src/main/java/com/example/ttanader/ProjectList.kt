package com.example.ttanader

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.models.ProjectList
import com.example.ttanader.models.Task

class ProjectList : AppCompatActivity() {

    private lateinit var adapter: ProjectListAdapter
    private val projectLists = mutableListOf<ProjectList>()

    private val isAdmin = true // TODO: Replace with actual role-checking logic
    private val currentUser = "user@example.com" // TODO: Replace with logged-in user email
    private val teamMembers = listOf("Alice", "Bob", "Charlie") // TODO: Fetch actual team members

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
            onAddTaskClick = { listIndex -> showAddTaskDialog(listIndex) }
        )

        recyclerView.adapter = adapter

        // Handle "Add List" button click
        findViewById<Button>(R.id.btnAddProject).setOnClickListener {
            showAddProjectDialog()
        }
    }

    // Show dialog to add a new project list
    private fun showAddProjectDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Project")

        val input = EditText(this)
        input.hint = "Enter New Project Title"
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setPadding(32, 16, 32, 16)

        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val listName = input.text.toString().trim()
            if (listName.isNotEmpty()) {
                val newList = ProjectList(name = listName, tasks = mutableListOf()) // Initialize tasks list
                projectLists.add(newList)
                adapter.notifyItemInserted(projectLists.size - 1)
                Toast.makeText(this, "Project added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Project name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    // Show dialog to add a task inside a project list
    private fun showAddTaskDialog(listIndex: Int) {
        if (listIndex !in projectLists.indices) {
            Toast.makeText(this, "Invalid project list", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Task")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(32, 16, 32, 16)

        val input = EditText(this)
        input.hint = "Enter task name"
        input.inputType = InputType.TYPE_CLASS_TEXT
        layout.addView(input)

        builder.setView(layout)

        builder.setPositiveButton("Add") { _, _ ->
            val taskName = input.text.toString().trim()
            if (taskName.isNotEmpty()) {
                val newTask = Task(name = taskName)
                projectLists[listIndex].tasks.add(newTask)
                adapter.notifyItemChanged(listIndex)
                Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
