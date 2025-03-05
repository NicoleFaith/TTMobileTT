package com.example.ttanader

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ManageMembersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MemberAdapter
    private var members = mutableListOf<Member>()
    private var isAdmin = false // Default to false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_members)

        recyclerView = findViewById(R.id.rvMembers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get logged-in user ID (Replace with actual user session logic)
        val loggedInUserId = getLoggedInUserId()

        // Fetch members and determine if user is admin
        members = getMembersFromDatabase()
        val projectCreatorId = getProjectCreatorId() // Fetch from database

        // Determine if the logged-in user is the project creator (admin)
        isAdmin = (loggedInUserId == projectCreatorId)

        // Initialize adapter
        adapter = MemberAdapter(members, isAdmin) { member ->
            removeMember(member)
        }
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.btnClose).setOnClickListener {
            finish()
        }
    }

    // Simulated function to get logged-in user ID (replace with actual logic)
    private fun getLoggedInUserId(): String {
        return "user_123" // Example user ID (Replace with actual user session data)
    }

    // Simulated function to get project creator ID (Replace with actual DB query)
    private fun getProjectCreatorId(): String {
        return "user_123" // Example: The creator of the project
    }

    private fun getMembersFromDatabase(): MutableList<Member> {
        return mutableListOf(
            Member("Jose", "user_123"), //sample
            Member("Protacio", "user_456"), //sample
            Member("Mercado", "user_789") //sample
        )
    }

    private fun removeMember(member: Member) {
        val position = members.indexOf(member)
        if (position != -1) {
            members.removeAt(position)
            adapter.notifyItemRemoved(position)
            adapter.notifyItemRangeChanged(position, members.size)
        }
    }
}

// Updated Member Data Class
data class Member(val name: String, val userId: String)

