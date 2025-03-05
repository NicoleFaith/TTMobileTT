package com.example.ttanader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.databinding.ItemConfirmedEmailBinding

// RecyclerView Adapter for displaying confirmed members with a better UI
class ConfirmedEmailsAdapter(
    private val emailList: List<String> // List of confirmed emails
) : RecyclerView.Adapter<ConfirmedEmailsAdapter.EmailViewHolder>() {

    // ViewHolder class to hold and bind the views for each email item
    class EmailViewHolder(private val binding: ItemConfirmedEmailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Function to bind email data to the UI components
        fun bind(email: String) {
            binding.tvEmail.text = email // Set the email text
        }
    }

    // Function to create a new ViewHolder when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val binding = ItemConfirmedEmailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmailViewHolder(binding) // Return a new instance of EmailViewHolder
    }

    // Function to bind data to the ViewHolder at a specific position
    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(emailList[position]) // Call bind() to set the email for this item
    }

    // Function to return the total number of email items
    override fun getItemCount(): Int = emailList.size
}
