package com.example.ttanader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.databinding.ActivityConfirmedEmailsAdapterBinding

class ConfirmedEmailsAdapter(private val emailList: MutableList<String>) :
    RecyclerView.Adapter<ConfirmedEmailsAdapter.ConfirmedEmailViewHolder>() {

    class ConfirmedEmailViewHolder(private val binding: ActivityConfirmedEmailsAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(email: String) {
            binding.tvConfirmedEmail.text = email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmedEmailViewHolder {
        val binding = ActivityConfirmedEmailsAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConfirmedEmailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConfirmedEmailViewHolder, position: Int) {
        holder.bind(emailList[position])
    }

    override fun getItemCount(): Int = emailList.size
}
