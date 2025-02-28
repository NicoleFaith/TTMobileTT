package com.example.ttanader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.models.Task
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.button.MaterialButton

class TaskAdapter(
    private val context: Context,
    private val tasks: MutableList<Task>,
    private val currentUser: String, // ✅ The logged-in user
    private val isAdmin: Boolean, // ✅ If the user is an admin
    private val teamMembers: List<String> // ✅ Dynamic list of team members
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.tvTaskName)
        val chipGroupStatus: ChipGroup = itemView.findViewById(R.id.chipGroupProgress)
        val chipTodo: Chip = itemView.findViewById(R.id.chipTodo)
        val chipInProgress: Chip = itemView.findViewById(R.id.chipInProgress)
        val chipDone: Chip = itemView.findViewById(R.id.chipDone)
        val editButton: MaterialButton = itemView.findViewById(R.id.btnEditTask)
        val assignButton: MaterialButton = itemView.findViewById(R.id.btnAssignMember)
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

        // 🎯 **Set Initial Chip Selection and Background Colors**
        updateChipUI(holder, task.status)

        // 🎯 **Only Assigned Members Can Change Status**
        holder.chipGroupStatus.setOnCheckedChangeListener { group, checkedId ->
            if (isAssignedMember) {
                val newStatus = when (checkedId) {
                    R.id.chipTodo -> "To Do"
                    R.id.chipInProgress -> "In Progress"
                    R.id.chipDone -> "Completed"
                    else -> task.status
                }
                updateTaskStatus(position, newStatus)
            } else {
                Toast.makeText(context, "Only assigned members can change status", Toast.LENGTH_SHORT).show()
                // Prevent status change by re-selecting the previous status
                group.clearCheck()
                when (task.status) {
                    "To Do" -> holder.chipTodo.isChecked = true
                    "In Progress" -> holder.chipInProgress.isChecked = true
                    "Completed" -> holder.chipDone.isChecked = true
                }
            }
        }

        // 🎯 **Admin Assigns Members**
        holder.assignButton.setOnClickListener {
            showAssignMemberDropdown(holder.assignButton, position)
        }

        // 🎯 **Admin Edits Task**
        holder.editButton.setOnClickListener {
            showEditTaskDialog(position)
        }
    }

    override fun getItemCount(): Int = tasks.size

    // ✅ **Update Task List Dynamically**
    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    // ✅ **Update Task Status**
    private fun updateTaskStatus(position: Int, newStatus: String) {
        if (tasks[position].status != newStatus) { // Only update if status changed
            tasks[position] = tasks[position].copy(status = newStatus)
            notifyItemChanged(position)
            Toast.makeText(context, "Task updated to $newStatus", Toast.LENGTH_SHORT).show()
        }
    }

    // ✅ **Set Chip UI Based on Status**
    private fun updateChipUI(holder: TaskViewHolder, status: String) {
        val context = holder.itemView.context

        when (status) {
            "To Do" -> {
                holder.chipTodo.isChecked = true
                holder.chipTodo.setChipBackgroundColorResource(R.color.todo_bg)
            }
            "In Progress" -> {
                holder.chipInProgress.isChecked = true
                holder.chipInProgress.setChipBackgroundColorResource(R.color.in_progress_bg)
            }
            "Completed" -> {
                holder.chipDone.isChecked = true
                holder.chipDone.setChipBackgroundColorResource(R.color.completed_bg)
            }
        }
    }

    // ✅ **Show Edit Task Dialog (Only Admins)**
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

    // ✅ **Show Assign Member Dropdown (Popup Menu)**
    private fun showAssignMemberDropdown(anchorView: View, position: Int) {
        val popupMenu = PopupMenu(context, anchorView)
        teamMembers.forEach { member ->
            popupMenu.menu.add(member)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val selectedMember = menuItem.title.toString()
            if (tasks[position].assignedMember != selectedMember) {
                tasks[position] = tasks[position].copy(assignedMember = selectedMember)
                notifyItemChanged(position)
                Toast.makeText(context, "Assigned to $selectedMember", Toast.LENGTH_SHORT).show()
            }
            true
        }
        popupMenu.show()
    }
}
