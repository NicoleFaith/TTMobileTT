package com.example.ttanader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.models.Task
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.button.MaterialButton

class TaskAdapter(
    private val context: Context,
    private var tasks: MutableList<Task>,
    private val currentUser: String,
    private val isAdmin: Boolean,
    private val teamMembers: List<String>
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.tvTaskName)
        val chipGroupStatus: ChipGroup = itemView.findViewById(R.id.chipGroupProgress)
        val chipTodo: Chip = itemView.findViewById(R.id.chipTodo)
        val chipInProgress: Chip = itemView.findViewById(R.id.chipInProgress)
        val chipDone: Chip = itemView.findViewById(R.id.chipDone)
        val assignButton: MaterialButton = itemView.findViewById(R.id.btnAssignMember)
        val assignedMember: TextView = itemView.findViewById(R.id.tvAssignedMember)
        val editButton: MaterialButton = itemView.findViewById(R.id.btnEditTask)

        fun bind(
            task: Task,
            isAdmin: Boolean,
            currentUser: String,
            teamMembers: List<String>,
            position: Int,
            updateTaskStatus: (Int, String) -> Unit,
            showAssignMemberDropdown: (View, Int, TextView) -> Unit
        ) {
            taskName.text = task.name
            assignedMember.text = task.assignedMember.ifEmpty { "Not Assigned" }

            // ✅ Admins can assign members
            assignButton.visibility = if (isAdmin) View.VISIBLE else View.GONE
            assignButton.setOnClickListener {
                showAssignMemberDropdown(assignButton, position, assignedMember)
            }

            // ✅ Set the correct chip based on task status
            resetChips(this, task.status)

            // ✅ Allow only assigned members to change status
            val isAssignedMember = task.assignedMember == currentUser
            chipGroupStatus.setOnCheckedChangeListener { _, checkedId ->
                if (isAssignedMember) {
                    val newStatus = when (checkedId) {
                        R.id.chipTodo -> "To Do"
                        R.id.chipInProgress -> "In Progress"
                        R.id.chipDone -> "Completed"
                        else -> task.status
                    }
                    updateTaskStatus(position, newStatus)
                } else {
                    Toast.makeText(itemView.context, "Only assigned members can change status", Toast.LENGTH_SHORT).show()
                    resetChips(this, task.status)
                }
            }
        }

        private fun resetChips(holder: TaskViewHolder, status: String) {
            holder.chipGroupStatus.clearCheck()
            when (status) {
                "To Do" -> holder.chipTodo.isChecked = true
                "In Progress" -> holder.chipInProgress.isChecked = true
                "Completed" -> holder.chipDone.isChecked = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, isAdmin, currentUser, teamMembers, position, ::updateTaskStatus, ::showAssignMemberDropdown)
    }

    override fun getItemCount(): Int = tasks.size

    // ✅ Update task status and refresh UI
    private fun updateTaskStatus(position: Int, newStatus: String) {
        if (tasks[position].status != newStatus) {
            tasks[position] = tasks[position].copy(status = newStatus)
            notifyItemChanged(position)
            Toast.makeText(context, "Task updated to $newStatus", Toast.LENGTH_SHORT).show()
        }
    }

    // ✅ Assign members to tasks dynamically
    private fun showAssignMemberDropdown(anchorView: View, position: Int, assignedMemberTextView: TextView) {
        val popupMenu = PopupMenu(context, anchorView)
        teamMembers.forEach { member -> popupMenu.menu.add(member) }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val selectedMember = menuItem.title.toString()
            if (tasks[position].assignedMember != selectedMember) {
                tasks[position] = tasks[position].copy(assignedMember = selectedMember)
                notifyItemChanged(position)
                assignedMemberTextView.text = selectedMember
                Toast.makeText(context, "Assigned to $selectedMember", Toast.LENGTH_SHORT).show()
            }
            true
        }
        popupMenu.show()
    }

    // ✅ Allow external updates to the task list
    fun updateTasks(newTasks: MutableList<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}
