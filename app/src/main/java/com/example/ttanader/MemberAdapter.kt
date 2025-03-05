package com.example.ttanader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(
    private val members: List<Member>,
    private val isAdmin: Boolean, // Admin status is passed from activity
    private val onDeleteClick: (Member) -> Unit
) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        holder.tvName.text = member.name

        // Show delete button only if user is admin and member is NOT the creator
        if (isAdmin) {
            holder.btnDelete.visibility = View.VISIBLE
            holder.btnDelete.setOnClickListener { onDeleteClick(member) }
        } else {
            holder.btnDelete.visibility = View.GONE
        }
    }

    override fun getItemCount() = members.size
}
