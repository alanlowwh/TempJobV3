package com.example.tempjobv3.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.tempjobv3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : Fragment(R.layout.fragment_login) {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val registerNowTextView = view.findViewById<TextView>(R.id.registerNow)

        loginButton.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Attempt to sign in with email and password
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign-in was successful, now check the user's role
                            checkUserRole(email)
                        } else {
                            // Sign-in failed, display an error message
                            Toast.makeText(
                                requireContext(),
                                "Login failed. Please check your credentials.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // Email or password field is empty
                Toast.makeText(
                    requireContext(),
                    "Please enter both email and password.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        registerNowTextView.setOnClickListener {
            // Navigate to the registration fragment
            findNavController().navigate(R.id.action_login_to_signUp)
        }
    }

    private fun checkUserRole(email: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        val usersRef: DatabaseReference = databaseReference.child("users")
        val adminRef: DatabaseReference = databaseReference.child("admin")

        // Check the user's role in the 'users' entity
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User found in 'users' entity, they have the 'customer' role
                    findNavController().navigate(R.id.action_login_to_profile)
                } else {
                    // User not found in 'users' entity, check in 'admin' entity
                    checkAdminRole(email)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(
                    requireContext(),
                    "Database error: " + databaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun checkAdminRole(email: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        val adminRef: DatabaseReference = databaseReference.child("admin")

        // Check the user's role in the 'admin' entity
        adminRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User found in 'admin' entity, they have the 'admin' role
                    findNavController().navigate(R.id.action_login_to_list_job)
                } else {
                    // User not found in 'admin' entity, handle other cases if needed
                    Toast.makeText(
                        requireContext(),
                        "User not found in either 'users' or 'admin' entity.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(
                    requireContext(),
                    "Database error: " + databaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
