package com.example.ttanader.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ttanader.AddMembers
import com.example.ttanader.ConfirmedEmailsAdapter
import com.example.ttanader.databinding.ActivityCreateBinding

class Create : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private val confirmedEmails = mutableListOf<String>()
    private lateinit var confirmedEmailsAdapter: ConfirmedEmailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        confirmedEmailsAdapter = ConfirmedEmailsAdapter(confirmedEmails)
        binding.rvConfirmedEmails.layoutManager = LinearLayoutManager(this)
        binding.rvConfirmedEmails.adapter = confirmedEmailsAdapter

        // Open AddMembers activity
        binding.NewProjectAddMembersBtn.setOnClickListener {
            val intent = Intent(this, AddMembers::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_MEMBERS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_MEMBERS && resultCode == Activity.RESULT_OK) {
            val emails = data?.getStringArrayListExtra("CONFIRMED_EMAILS")
            emails?.let {
                confirmedEmails.clear()
                confirmedEmails.addAll(it)
                confirmedEmailsAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD_MEMBERS = 100
    }
}
