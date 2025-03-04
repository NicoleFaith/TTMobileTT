package com.example.ttanader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttanader.models.ProjectList
import com.example.ttanader.models.Task

class ProjectListAdapter(
    private val lists: MutableList<ProjectList>,
    private val isAdmin: Boolean, // ✅ Check if the user is an admin
    private val currentUser: String, // ✅ The logged-in user
    private val teamMembers: List<String>, // ✅ List of team members
    private val onAddTaskClick: (Int) -> Unit // ✅ Callback function to handle adding tasks
) : RecyclerView.Adapter<ProjectListAdapter.ProjectListViewHolder>() {

    class ProjectListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listTitle: TextView = itemView.findViewById(R.id.tvListTitle)
        val btnAddTask: Button = itemView.findViewById(R.id.btnAddTask)
        val rvTasks: RecyclerView = itemView.findViewById(R.id.rvTasks)

        fun bind(
            list: ProjectList,
            isAdmin: Boolean,
            currentUser: String,
            teamMembers: List<String>,
            onAddTaskClick: (Int) -> Unit,
            position: Int
        ) {
            listTitle.text = list.name
            btnAddTask.visibility = if (isAdmin) View.VISIBLE else View.GONE

            // ✅ Set up RecyclerView for tasks
            val taskAdapter = TaskAdapter(
                context = itemView.context,
                tasks = list.tasks.toMutableList(),
                currentUser = currentUser,
                isAdmin = isAdmin,
                teamMembers = teamMembers
            )

            rvTasks.layoutManager = LinearLayoutManager(itemView.context)
            rvTasks.adapter = taskAdapter

            btnAddTask.setOnClickListener {
                onAddTaskClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ProjectListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectListViewHolder, position: Int) {
        val list = lists[position]
        holder.bind(list, isAdmin, currentUser, teamMembers, onAddTaskClick, position)
    }

    override fun getItemCount(): Int = lists.size

    // ✅ Update a specific project's task list
    fun updateTaskList(listIndex: Int, updatedTasks: MutableList<Task>) {
        if (listIndex in lists.indices) {
            lists[listIndex].tasks = updatedTasks
            notifyItemChanged(listIndex)
        }
    }
}
