package com.example.ttanader

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ttanader.create.Create
import com.example.ttanader.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use View Binding to inflate the layout
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up click listener for the Create Team button
        binding.CreateWorkspaceBtn.setOnClickListener {
            val intent = Intent(requireContext(), Create::class.java)
            startActivity(intent)
        }

        // TODO: Load the projects assigned to or created by the user
        loadProjects()
    }

    private fun loadProjects() {
        // TODO: Fetch and display projects from the database
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
