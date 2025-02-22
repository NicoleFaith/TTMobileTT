package com.example.ttanader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.databinding.ActivityAddMembersBinding
import com.example.ttanader.databinding.ItemEmailBinding
import android.view.LayoutInflater
import android.view.ViewGroup

class AddMembers : AppCompatActivity() {

    private lateinit var binding: ActivityAddMembersBinding
    private lateinit var emailAdapter: EmailAdapter
    private val emailList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityAddMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView with Adapter
        emailAdapter = EmailAdapter(emailList)
        binding.rvEmails.apply {
            layoutManager = LinearLayoutManager(this@AddMembers)
            adapter = emailAdapter
        }

        // Handle button click to add email
        binding.btnAddEmail.setOnClickListener {
            val email = binding.EnterEmailMember.text.toString().trim()
            if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailAdapter.addEmail(email) // Add email to the RecyclerView
                binding.EnterEmailMember.text?.clear() // Clear input field after adding
            } else {
                binding.EnterEmailMember.error = "Enter a valid email"
            }
        }
    }
}

// ✅ Corrected Email Adapter
class EmailAdapter(private val emailList: MutableList<String>) :
    RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    class EmailViewHolder(private val binding: ItemEmailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(email: String) {
            binding.tvEmail.text = email // ✅ Correctly bind email to TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val binding = ItemEmailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(emailList[position])
    }

    override fun getItemCount(): Int = emailList.size

    fun addEmail(email: String) {
        emailList.add(email)
        notifyDataSetChanged() // 🔥 Ensures all items refresh properly
    }
}
