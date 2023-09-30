package com.example.tempjobv3.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.tempjobv3.R

class FirstPage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_page, container, false)

        // Find the "Sign Up" button and set a click listener
        val signUpButton = view.findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            // Navigate to the sign-up screen when the "Sign Up" button is clicked
            findNavController().navigate(R.id.action_firstPage_to_signUp)
        }

        // Find the "Log In" button and set a click listener
        val logInButton = view.findViewById<Button>(R.id.logInButton)
        logInButton.setOnClickListener {
            // Navigate to the login screen when the "Log In" button is clicked
            findNavController().navigate(R.id.action_firstPage_to_login)
        }

        return view
    }
}