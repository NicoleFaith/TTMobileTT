package com.example.ttanader

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ManageMembersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MemberAdapter
    private var members = mutableListOf<Member>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_members)

        recyclerView = findViewById(R.id.rvMembers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val isAdmin = true // Change based on logged-in user
        members = getMembers() // Fetch members from SQLite or API

        adapter = MemberAdapter(members, isAdmin) { member ->
            removeMember(member)
        }
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.btnClose).setOnClickListener {
            finish()
        }
    }

    private fun getMembers(): MutableList<Member> {
        return mutableListOf(
            Member("John Doe", true),
            Member("Jane Smith", false),
            Member("Alex Brown", false)
        )
    }

    private fun removeMember(member: Member) {
        members.remove(member)
        adapter.notifyDataSetChanged()
    }
}

data class Member(val name: String, val isAdmin: Boolean)
