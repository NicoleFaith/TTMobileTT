package com.example.ttanader

import android.content.Context
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
    private val currentUser: String, // ✅ The logged-in user
    private val isAdmin: Boolean, // ✅ If the user is an admin
    private val teamMembers: List<String> // ✅ Dynamic list of team members
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.tvTaskName)
        val toggleStatus: ToggleButton = itemView.findViewById(R.id.toggleTaskStatus)
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

        // ✅ Check if the logged-in user is the assigned member
        val isAssignedMember = task.assignedMember == currentUser

        // 🎯 **Only Admins Can Edit or Assign Members**
        holder.editButton.visibility = if (isAdmin) View.VISIBLE else View.GONE
        holder.assignButton.visibility = if (isAdmin) View.VISIBLE else View.GONE

        // 🎯 **Only Assigned Members Can Change Status**
        holder.toggleStatus.isEnabled = isAssignedMember
        holder.toggleStatus.textOn = "Completed"
        holder.toggleStatus.textOff = "Ongoing"
        holder.toggleStatus.isChecked = task.status == "Completed"

        holder.toggleStatus.setOnCheckedChangeListener { _, isChecked ->
            if (isAssignedMember) {
                updateTaskStatus(position, if (isChecked) "Completed" else "Ongoing")
            }
        }

        // 🎯 **Admin Edits Task**
        holder.editButton.setOnClickListener {
            showEditTaskDialog(position)
        }

        // 🎯 **Admin Assigns Members**
        holder.assignButton.setOnClickListener {
            showAssignMemberDialog(position)
        }
    }

    override fun getItemCount(): Int = tasks.size

    // ✅ **New Method: Update Task List Dynamically**
    fun updateTasks(newTasks: List<Task>) {
        tasks.clear() // Clear the existing task list
        tasks.addAll(newTasks) // Add the new tasks
        notifyDataSetChanged() // Refresh RecyclerView
    }

    // ✅ Update task status
    private fun updateTaskStatus(position: Int, newStatus: String) {
        if (tasks[position].status != newStatus) { // Only update if status changed
            tasks[position] = tasks[position].copy(status = newStatus)
            notifyItemChanged(position)
            Toast.makeText(context, "Task updated to $newStatus", Toast.LENGTH_SHORT).show()
        }
    }

    // ✅ Show Edit Task Dialog (Only Admins Can Use)
    private fun showEditTaskDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit Task")

        val input = EditText(context)
        input.setText(tasks[position].name)
        builder.setView(input)

        builder.setPositiveButton("Save") { _, _ ->
            val newName = input.text.toString().trim()
            if (newName.isNotEmpty() && newName != tasks[position].name) {
                tasks[position] = tasks[position].copy(name = newName)
                notifyItemChanged(position)
            } else {
                Toast.makeText(context, "Task name cannot be empty or the same", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    // ✅ Show Assign Member Dialog (Only Admins Can Use)
    private fun showAssignMemberDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Assign Member")

        val spinner = Spinner(context)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, teamMembers)
        spinner.adapter = adapter

        builder.setView(spinner)

        builder.setPositiveButton("Assign") { _, _ ->
            val selectedMember = spinner.selectedItem.toString()
            if (tasks[position].assignedMember != selectedMember) { // Avoid unnecessary updates
                tasks[position] = tasks[position].copy(assignedMember = selectedMember)
                notifyItemChanged(position)
                Toast.makeText(context, "Assigned to $selectedMember", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
