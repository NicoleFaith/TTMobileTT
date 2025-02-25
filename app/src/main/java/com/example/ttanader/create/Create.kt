package com.example.ttanader.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttanader.AddMembers
import com.example.ttanader.ConfirmedEmailsAdapter
import com.example.ttanader.UserProject// Change UserProject to TeamDashboard
import com.example.ttanader.databinding.ActivityCreateBinding

class Create : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding  // View binding for accessing UI elements
    private val confirmedEmails = mutableListOf<String>() // List to store confirmed emails
    private lateinit var confirmedEmailsAdapter: ConfirmedEmailsAdapter // Adapter for displaying confirmed emails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView for displaying confirmed emails
        confirmedEmailsAdapter = ConfirmedEmailsAdapter(confirmedEmails)
        binding.rvConfirmedEmails.layoutManager = LinearLayoutManager(this)
        binding.rvConfirmedEmails.adapter = confirmedEmailsAdapter

        // Initially hide the "Edit Members" button (will be shown when members are added)
        binding.btnEditMembers.visibility = View.GONE

        // Open AddMembers activity when clicking "Add Members" button
        binding.NewProjectAddMembersBtn.setOnClickListener {
            openEditMembersScreen()
        }

        // Open AddMembers activity for editing members
        binding.btnEditMembers.setOnClickListener {
            openEditMembersScreen()
        }

        // Confirm Team button (Navigates to TeamDashboard)
        binding.ConfirmTeam.setOnClickListener {
            confirmTeam()
        }
    }

    // Opens the AddMembers activity and passes the current list of confirmed emails
    private fun openEditMembersScreen() {
        val intent = Intent(this, AddMembers::class.java)
        intent.putStringArrayListExtra("EXISTING_EMAILS", ArrayList(confirmedEmails)) // Pass current emails
        startActivityForResult(intent, REQUEST_CODE_ADD_MEMBERS) // Start AddMembers activity
    }

    // Handles the result returned from AddMembers activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_MEMBERS && resultCode == Activity.RESULT_OK) {
            val emails = data?.getStringArrayListExtra("CONFIRMED_EMAILS") // Retrieve confirmed emails
            emails?.let {
                confirmedEmails.clear() // Clear existing list
                confirmedEmails.addAll(it) // Add new confirmed emails
                confirmedEmailsAdapter.notifyDataSetChanged() // Notify adapter to refresh UI

                // Show "Edit Members" button only if there are confirmed members
                binding.btnEditMembers.visibility = if (confirmedEmails.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    // Confirm Team - Validates input and navigates to TeamDashboard
    private fun confirmTeam() {
        val teamName = binding.teamNameCreated.text.toString().trim() // Get team name input

        if (teamName.isEmpty()) {
            Toast.makeText(this, "Please enter a team name!", Toast.LENGTH_SHORT).show()
            return
        }

        if (confirmedEmails.isEmpty()) {
            Toast.makeText(this, "Please add at least one member!", Toast.LENGTH_SHORT).show()
            return
        }

        // Pass team name and members to TeamDashboard activity
        val intent = Intent(this, UserProject::class.java)
        intent.putExtra("TEAM_NAME", teamName) // Send team name
        intent.putStringArrayListExtra("TEAM_MEMBERS", ArrayList(confirmedEmails)) // Send team members
        startActivity(intent) // Navigate to TeamDashboard
        finish() // Close Create activity
    }

    companion object {
        private const val REQUEST_CODE_ADD_MEMBERS = 100 // Request code for starting AddMembers activity
    }
}
