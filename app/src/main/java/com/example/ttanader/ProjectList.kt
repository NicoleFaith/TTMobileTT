package com.example.ttanader

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.adapters.ProjectListAdapter
import com.example.ttanader.models.ProjectList
import com.example.ttanader.models.Task

class ProjectList : AppCompatActivity() {

    private lateinit var adapter: ProjectListAdapter
    private val projectLists = mutableListOf<ProjectList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.project_list)

        // Set up RecyclerView for project lists
        val recyclerView = findViewById<RecyclerView>(R.id.rvProjectLists)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = ProjectListAdapter(projectLists) { listIndex -> showAddTaskDialog(listIndex) }
        recyclerView.adapter = adapter

        // Handle "Add List" button click
        findViewById<Button>(R.id.btnAddList).setOnClickListener {
            showAddListDialog()
        }
    }

    private fun showAddListDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Project")

        val input = EditText(this)
        input.hint = "Enter New Project Title"
        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val listName = input.text.toString().trim()
            if (listName.isNotEmpty()) {
                val newList = ProjectList(listName)
                adapter.addList(newList)
            } else {
                Toast.makeText(this, "List name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun showAddTaskDialog(listIndex: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Task")

        val input = EditText(this)
        input.hint = "Enter task name"
        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val taskName = input.text.toString().trim()
            if (taskName.isNotEmpty()) {
                projectLists[listIndex].tasks.add(Task(taskName)) // Add task to selected list
                adapter.notifyItemChanged(listIndex) // Update UI
            } else {
                Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
