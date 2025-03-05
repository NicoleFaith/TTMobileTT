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

        // Retrieve existing emails from intent
        emailList.addAll(intent.getStringArrayListExtra("EXISTING_EMAILS") ?: emptyList())

        // Setup RecyclerView
        emailAdapter = EmailAdapter(emailList) { updateConfirmButtonVisibility() }
        binding.rvEmails.apply {
            layoutManager = LinearLayoutManager(this@AddMembers)
            adapter = emailAdapter
        }

        updateConfirmButtonVisibility() // Hide confirm button if empty

        // Add Email Button Click Listener
        binding.btnAddEmail.setOnClickListener { addEmail() }

        // Confirm Members Button Click Listener
        binding.btnConfirmEmails.setOnClickListener {
            val resultIntent = Intent().apply {
                putStringArrayListExtra("CONFIRMED_EMAILS", ArrayList(emailList))
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun addEmail() {
        val email = binding.EnterEmailMember.text.toString().trim()

        when {
            email.isEmpty() -> binding.EnterEmailMember.error = "Enter an email"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                binding.EnterEmailMember.error = "Enter a valid email"
            emailList.contains(email) ->
                binding.EnterEmailMember.error = "Email already added"
            else -> {
                emailAdapter.addEmail(email)
                binding.EnterEmailMember.text?.clear()
                updateConfirmButtonVisibility()
            }
        }
    }

    private fun updateConfirmButtonVisibility() {
        binding.btnConfirmEmails.visibility = if (emailList.isNotEmpty()) View.VISIBLE else View.GONE
    }
}

// Adapter for Email List
class EmailAdapter(
    private val emailList: MutableList<String>,
    private val onListChanged: () -> Unit // Callback to update button visibility
) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    class EmailViewHolder(private val binding: ItemEmailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(email: String, onRemove: (Int) -> Unit) {
            binding.tvEmail.text = email
            binding.btnRemove.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    showDeleteConfirmationDialog(binding.root.context, position, onRemove)
                }
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
        onListChanged()
    }

    private fun removeEmail(position: Int) {
        if (position in emailList.indices) { // Prevent crash if position is invalid
            emailList.removeAt(position)
            notifyItemRemoved(position)
            onListChanged()
        }
    }
}
