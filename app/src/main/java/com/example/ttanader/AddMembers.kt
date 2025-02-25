package com.example.ttanader

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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

        binding = ActivityAddMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve existing emails (if any)
        val existingEmails = intent.getStringArrayListExtra("EXISTING_EMAILS") ?: ArrayList()
        emailList.addAll(existingEmails)

        // Initialize RecyclerView with Adapter
        emailAdapter = EmailAdapter(emailList) { updateConfirmButtonVisibility() }
        binding.rvEmails.layoutManager = LinearLayoutManager(this)
        binding.rvEmails.adapter = emailAdapter

        // Initially hide the Confirm button if no members
        updateConfirmButtonVisibility()

        // Add new email
        binding.btnAddEmail.setOnClickListener {
            val email = binding.EnterEmailMember.text.toString().trim()
            if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!emailList.contains(email)) {
                    emailAdapter.addEmail(email)
                    binding.EnterEmailMember.text?.clear()
                    updateConfirmButtonVisibility() // Check visibility after adding
                } else {
                    binding.EnterEmailMember.error = "Email already added"
                }
            } else {
                binding.EnterEmailMember.error = "Enter a valid email"
            }
        }

        // Confirm and return the email list
        binding.btnConfirmEmails.setOnClickListener {
            val resultIntent = Intent().apply {
                putStringArrayListExtra("CONFIRMED_EMAILS", ArrayList(emailList))
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    // Function to show/hide confirm button based on email list size
    private fun updateConfirmButtonVisibility() {
        binding.btnConfirmEmails.visibility = if (emailList.isNotEmpty()) View.VISIBLE else View.GONE
    }
}

// Email Adapter with Remove Function
class EmailAdapter(
    private val emailList: MutableList<String>,
    private val onListChanged: () -> Unit // Callback to update button visibility
) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    class EmailViewHolder(private val binding: ItemEmailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(email: String, onRemove: (Int) -> Unit) {
            binding.tvEmail.text = email

            // Remove email
            binding.btnRemove.setOnClickListener {
                showDeleteConfirmationDialog(binding.root.context, adapterPosition, onRemove)
            }
        }

        private fun showDeleteConfirmationDialog(
            context: android.content.Context,
            position: Int,
            onRemove: (Int) -> Unit
        ) {
            AlertDialog.Builder(context)
                .setTitle("Remove Email?")
                .setMessage("Are you sure you want to remove this email?")
                .setPositiveButton("Delete") { _, _ -> onRemove(position) }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val binding = ItemEmailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(emailList[position]) { removeEmail(it) }
    }

    override fun getItemCount(): Int = emailList.size

    fun addEmail(email: String) {
        emailList.add(email)
        notifyItemInserted(emailList.size - 1)
        onListChanged() // Notify activity that list has changed
    }

    private fun removeEmail(position: Int) {
        emailList.removeAt(position)
        notifyItemRemoved(position)
        onListChanged() // Notify activity that list has changed
    }
}
