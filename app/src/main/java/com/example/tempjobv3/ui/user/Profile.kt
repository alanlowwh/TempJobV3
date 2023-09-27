package com.example.tempjobv3.ui.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.tempjobv3.R
import com.example.tempjobv3.data.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class Profile : Fragment() {

    private lateinit var usernameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize the TextView for displaying the username
        usernameTextView = view.findViewById(R.id.userName)

        // Check if the user is logged in
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // User is not logged in, navigate to the login page
            findNavController().navigate(R.id.action_profile_to_login)
        } else {
            // User is logged in, fetch and display user data
            fetchUserData()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editProfile = view.findViewById<TextView>(R.id.editProfile)
        // Handle click on "Edit Profile" TextView
        editProfile.setOnClickListener {
            // Navigate to the "Edit Profile" destination using the defined action
            findNavController().navigate(R.id.action_profile_to_editProfile)
        }

        val emailPassword = view.findViewById<TextView>(R.id.emailPassword)
        // Handle click on "emailPassword" TextView
        emailPassword.setOnClickListener {
            // Navigate to the "emailPassword" destination using the defined action
            findNavController().navigate(R.id.action_profile_to_changePassword)
        }

        val logoutTextView = view.findViewById<TextView>(R.id.logout)
        logoutTextView.setOnClickListener {
            // Call the logout function when the "Logout" TextView is clicked
            logout()
        }
    }

    private fun fetchUserData() {
        // Get the current user's UID from Firebase Authentication
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Reference to the Firebase Realtime Database
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

        // Reference to the "users" node in the database
        val usersRef: DatabaseReference = databaseReference.child("users")

        // Query the database to retrieve the user's data based on their UID
        val userQuery: Query = usersRef.child(userId!!)

        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists in the database
                    val user = dataSnapshot.getValue(User::class.java)

                    // Log the retrieved user data (for debugging)
                    Log.d("UserProfile", "User Name: ${user?.name}")

                    // Update the UI with the user's data
                    user?.let {
                        // Set the username in the TextView
                        usernameTextView.text = it.name
                    }
                } else {
                    // Data does not exist at the specified location
                    // Handle this case if necessary
                    Log.d("UserProfile", "No data found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Log.e("UserProfile", "Database Error: ${databaseError.message}")
            }
        })
    }

    private fun logout() {
        // Sign out the user from Firebase Authentication
        FirebaseAuth.getInstance().signOut()

        // After signing out, navigate the user to the login screen (or any desired destination)
        // You can use findNavController() to navigate to the appropriate destination
        findNavController().navigate(R.id.action_profile_to_login)
    }

}