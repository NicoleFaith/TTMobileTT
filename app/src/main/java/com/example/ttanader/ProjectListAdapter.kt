package com.example.ttanader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.example.ttanader.R
import com.example.ttanader.TaskAdapter
import com.example.ttanader.models.ProjectList
import com.example.ttanader.models.Task

class ProjectListAdapter(
    private val lists: MutableList<ProjectList>,
    private val onAddTaskClick: (Int) -> Unit // Callback function to handle adding tasks
) : RecyclerView.Adapter<ProjectListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listTitle: TextView = itemView.findViewById(R.id.tvListTitle)
        val btnAddTask: Button = itemView.findViewById(R.id.btnAddTask)
        val rvTasks: RecyclerView = itemView.findViewById(R.id.rvTasks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = lists[position]
        holder.listTitle.text = list.name

        // Set up Task RecyclerView
        holder.rvTasks.layoutManager = LinearLayoutManager(holder.itemView.context)
        val taskAdapter = TaskAdapter(holder.itemView.context, list.tasks)
        holder.rvTasks.adapter = taskAdapter

        // Handle Add Task Button Click
        holder.btnAddTask.setOnClickListener {
            onAddTaskClick(position) // Trigger the callback when clicking "Add Task"
        }
    }

    override fun getItemCount(): Int = lists.size

    fun addList(newList: ProjectList) {
        lists.add(newList)
        notifyItemInserted(lists.size - 1)
    }
}
