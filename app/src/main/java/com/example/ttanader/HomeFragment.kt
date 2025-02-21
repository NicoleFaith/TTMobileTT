package com.example.ttanader

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.ttanader.create.Create

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button by its ID
        val button = view.findViewById<Button>(R.id.CreateWorkspaceBtn) // Change "myButton" to your actual Button ID

        // Set OnClickListener to navigate to the new activity
        button.setOnClickListener {
            val intent = Intent(requireContext(), Create::class.java) // Replace with your activity
            startActivity(intent)
        }
    }
}
