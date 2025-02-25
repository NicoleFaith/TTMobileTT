package com.example.ttanader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.models.Task

class TaskAdapter(
    private val context: Context,
    private val tasks: MutableList<Task>
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.tvTaskName)
        val checkBox: CheckBox = itemView.findViewById(R.id.cbTaskCompleted)
        val editButton: Button = itemView.findViewById(R.id.btnEditTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.name

        // Handle Edit Task Button
        holder.editButton.setOnClickListener {
            showEditTaskDialog(position)
        }
    }

    override fun getItemCount(): Int = tasks.size

    private fun showEditTaskDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Task")

        val input = android.widget.EditText(context)
        input.setText(tasks[position].name)
        builder.setView(input)

        builder.setPositiveButton("Save") { _, _ ->
            val newName = input.text.toString().trim()
            if (newName.isNotEmpty()) {
                tasks[position] = Task(newName)
                notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
