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
import com.google.firebase.database.*

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

        // Check the user's role in the 'users' entity
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User found in 'users' entity, check their role
                    val user = dataSnapshot.children.firstOrNull()
                    val role = user?.child("role")?.value as? String

                    if (role == "admin") {
                        // User has the 'admin' role, navigate to the admin screen
                        findNavController().navigate(R.id.action_login_to_list_job)
                    } else {
                        // User has the 'customer' role, navigate to the profile
                        findNavController().navigate(R.id.action_login_to_profile)
                    }
                } else {
                    // User not found in 'users' entity
                    Toast.makeText(
                        requireContext(),
                        "User not found in 'users' entity.",
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
