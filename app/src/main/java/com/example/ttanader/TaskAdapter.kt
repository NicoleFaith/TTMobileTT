package com.example.ttanader

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.models.Task

class TaskAdapter(
    private val context: Context,
    private val tasks: MutableList<Task>,
    private val isAdmin: Boolean, // Check if the user is an admin
    private val currentUser: String, // The logged-in user
    private val teamMembers: List<String> // List of all team members
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.tvTaskName)
        val checkBox: CheckBox = itemView.findViewById(R.id.cbTaskCompleted)
        val editButton: Button = itemView.findViewById(R.id.btnEditTask)
        val assignButton: Button = itemView.findViewById(R.id.btnAssignMember)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskName.text = task.name

        // Set checkbox without triggering unnecessary changes
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = task.isCompleted

        // Admins can assign members
        holder.assignButton.visibility = if (isAdmin) View.VISIBLE else View.GONE
        holder.assignButton.setOnClickListener { showAssignMemberDialog(position) }

        // Allow only assigned members or admins to update task status
        val canUpdateTask = isAdmin || task.assignedMember == currentUser
        holder.checkBox.isEnabled = canUpdateTask

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (canUpdateTask && task.isCompleted != isChecked) {
                tasks[position] = task.copy(isCompleted = isChecked)
                notifyItemChanged(position)
            }
        }

        // Only admin can edit tasks
        holder.editButton.visibility = if (isAdmin) View.VISIBLE else View.GONE
        holder.editButton.setOnClickListener { showEditTaskDialog(position) }

        // Open Task Details Page on Click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TaskDetails::class.java).apply {
                putExtra("TASK_NAME", task.name)
                putExtra("ASSIGNED_MEMBER", task.assignedMember ?: "Not Assigned")
                putExtra("TASK_COMPLETED", task.isCompleted)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = tasks.size

    // Update tasks without recreating adapter
    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    private fun showEditTaskDialog(position: Int) {
        val task = tasks[position]

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Task")

        val input = EditText(context)
        input.setText(task.name)
        builder.setView(input)

        builder.setPositiveButton("Save") { _, _ ->
            val newName = input.text.toString().trim()
            if (newName.isNotEmpty()) {
                tasks[position] = task.copy(name = newName)
                notifyItemChanged(position)
            } else {
                Toast.makeText(context, "Task name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun showAssignMemberDialog(position: Int) {
        val task = tasks[position]

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Assign Member")

        val spinner = Spinner(context).apply {
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, teamMembers)
        }
        builder.setView(spinner)

        builder.setPositiveButton("Assign") { _, _ ->
            val selectedMember = spinner.selectedItem.toString()
            if (task.assignedMember != selectedMember) {
                tasks[position] = task.copy(assignedMember = selectedMember)
                notifyItemChanged(position)
                Toast.makeText(context, "Assigned to $selectedMember", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
