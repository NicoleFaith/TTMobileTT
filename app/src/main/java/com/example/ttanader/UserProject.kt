package com.example.ttanader

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var leftArrow: ImageView
    private lateinit var rightArrow: ImageView

    private val projectLists = mutableListOf<ProjectList>()
    private val currentUser = "user123"
    private val isAdmin = true
    private val teamMembers = listOf("Alice", "Bob", "Charlie")
    private var hasShownScrollHint = false  // Prevents repeated toast messages

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_project)

        // Set Team Name in Header
        val teamName = intent.getStringExtra("TEAM_NAME") ?: "No Team Name"
        findViewById<TextView>(R.id.tvTeamTitle)?.text = teamName

        recyclerView = findViewById(R.id.rvProjectLists)
        leftArrow = findViewById(R.id.leftArrow)
        rightArrow = findViewById(R.id.rightArrow)

        setupRecyclerView()

        // Handle "Add List" Button Click
        val btnAddProject = findViewById<FloatingActionButton>(R.id.btnAddProject)
        btnAddProject.setOnClickListener {
            showAddListDialog()
        }

        // Handle Back Button Click to go to HomeFragment
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            navigateToHome()
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = ProjectListAdapter(
            lists = projectLists,
            isAdmin = isAdmin,
            currentUser = currentUser,
            teamMembers = teamMembers
        ) { listIndex -> showAddTaskDialog(listIndex) }

        recyclerView.adapter = adapter

        // Initial visibility check for arrows
        updateArrowVisibility()

        // Scroll Listener to show/hide arrows dynamically
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                updateArrowVisibility()
            }
        })

        // Show scroll hint only once when there are enough projects
        recyclerView.post {
            if (!hasShownScrollHint && projectLists.size > 1) {
                showToast("Swipe left/right to see more projects")
                hasShownScrollHint = true
            }
        }
    }

    private fun updateArrowVisibility() {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()

        leftArrow.visibility = if (firstVisibleItem > 0) View.VISIBLE else View.GONE
        rightArrow.visibility = if (lastVisibleItem < adapter.itemCount - 1) View.VISIBLE else View.GONE
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
                recyclerView.post { updateArrowVisibility() }
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

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}
