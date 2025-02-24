package com.example.ttanader

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.databinding.ActivityAddMembersBinding
import com.example.ttanader.databinding.ItemEmailBinding

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
        binding.rvEmails.layoutManager = LinearLayoutManager(this)
        binding.rvEmails.adapter = emailAdapter

        // Handle button click to add email
        binding.btnAddEmail.setOnClickListener {
            val email = binding.EnterEmailMember.text.toString().trim()
            if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailAdapter.addEmail(email) // Add email to RecyclerView
                binding.EnterEmailMember.text?.clear() // Clear input field after adding
            } else {
                binding.EnterEmailMember.error = "Enter a valid email"
            }
        }

        // Confirm and send the selected emails back to Create activity
        binding.btnConfirmEmails.setOnClickListener {
            val resultIntent = Intent().apply {
                putStringArrayListExtra("CONFIRMED_EMAILS", ArrayList(emailList))
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}

class EmailAdapter(private val emailList: MutableList<String>) :
    RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    class EmailViewHolder(private val binding: ItemEmailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(email: String, onRemove: (Int) -> Unit) {
            binding.tvEmail.text = email

            // Show confirmation dialog before removing email
            binding.btnRemove.setOnClickListener {
                showDeleteConfirmationDialog(binding.root.context, adapterPosition, onRemove)
            }
        }

        private fun showDeleteConfirmationDialog(context: android.content.Context, position: Int, onRemove: (Int) -> Unit) {
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Remove Member?")
                .setMessage("Are you sure you want to remove this email?")
                .setPositiveButton("Delete") { _, _ ->
                    onRemove(position) // Delete email if confirmed
                }
                .setNegativeButton("Cancel", null) // Dismiss dialog if canceled
                .create()

            alertDialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val binding = ItemEmailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(emailList[position]) { pos -> removeEmail(pos) }
    }

    override fun getItemCount(): Int = emailList.size

    fun addEmail(email: String) {
        emailList.add(email)
        notifyItemInserted(emailList.size - 1)
    }

    private fun removeEmail(position: Int) {
        emailList.removeAt(position)
        notifyItemRemoved(position)
    }
}
