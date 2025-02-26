package com.example.ttanader

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.models.ProjectList
import com.example.ttanader.models.Task

class UserProject : AppCompatActivity() {

    private lateinit var adapter: ProjectListAdapter
    private val projectLists = mutableListOf<ProjectList>() // ✅ Declare Mutable List of ProjectList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_project)

        // Set Team Name in Header
        val teamName = intent.getStringExtra("TEAM_NAME")
        findViewById<TextView>(R.id.tvTeamTitle).text = teamName ?: "No Team Name"

        // Set up RecyclerView for Project Lists
        val recyclerView = findViewById<RecyclerView>(R.id.rvProjectLists)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = ProjectListAdapter(projectLists) { listIndex -> showAddTaskDialog(listIndex) }
        recyclerView.adapter = adapter

        // Handle "Add List" Button Click
        findViewById<Button>(R.id.btnAddList).setOnClickListener {
            showAddListDialog()
        }
    }

    private fun showAddListDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Project")

        val input = EditText(this)
        input.hint = "Enter New Project name"
        builder.setView(input)

        builder.setPositiveButton("Add") { _, _ ->
            val listName = input.text.toString().trim()
            if (listName.isNotEmpty()) {
                val newList = ProjectList(listName, mutableListOf()) // ✅ Create a New Project List
                projectLists.add(newList)  // ✅ Add it to the List
                adapter.notifyDataSetChanged() // ✅ Refresh RecyclerView
            } else {
                Toast.makeText(this, "Project name cannot be empty", Toast.LENGTH_SHORT).show()
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
                projectLists[listIndex].tasks.add(Task(taskName))  // ✅ Add task to selected list
                adapter.notifyItemChanged(listIndex)  // ✅ Update UI
            } else {
                Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
