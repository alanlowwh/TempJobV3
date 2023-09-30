package com.example.tempjobv3.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tempjobv3.R
import com.example.tempjobv3.data.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditProfile : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var jobTitleSpinner: Spinner
    private lateinit var updateButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        // Initialize UI elements
        nameEditText = view.findViewById(R.id.currentName)
        phoneEditText = view.findViewById(R.id.editTextPhone)
        genderSpinner = view.findViewById(R.id.spinnerGender)
        jobTitleSpinner = view.findViewById(R.id.spinnerJobTitle)
        updateButton = view.findViewById(R.id.updateButton)

        // Add an OnClickListener to the "Edit Profile" button
        updateButton.setOnClickListener {
            // Call the function to update the profile
            updateProfile()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch user data from Firebase Database and update UI
        fetchUserData()

        setupSpinner(R.id.spinnerGender, R.array.gender_options)
        setupSpinner(R.id.spinnerJobTitle, R.array.job_title_options)
    }

    private fun setupSpinner(spinnerId: Int, arrayResource: Int) {
        val spinner = view?.findViewById<Spinner>(spinnerId)
        spinner?.let {
            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayResource,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            it.adapter = adapter
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

                    // Update the UI with the user's data
                    user?.let {
                        // Set user data to UI elements
                        nameEditText.setText(it.name)
                        phoneEditText.setText(it.phone)

                        // Set selected item in gender spinner (if applicable)
                        setSpinnerSelection(genderSpinner, it.gender)

                        // Set selected item in job title spinner (if applicable)
                        setSpinnerSelection(jobTitleSpinner, it.jobTitle)
                    }
                } else {
                    // Data does not exist at the specified location
                    // Handle this case if necessary
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }

    // Function to set the selected item in a spinner
    private fun setSpinnerSelection(spinner: Spinner, selectedValue: String) {
        val adapter = spinner.adapter as ArrayAdapter<String>
        val position = adapter.getPosition(selectedValue)
        if (position != -1) {
            spinner.setSelection(position)
        }
    }

    // Function to update the user's profile data
    private fun updateProfile() {
        // Get the updated data from the UI elements
        val updatedName = nameEditText.text.toString()
        val updatedPhone = phoneEditText.text.toString()
        val updatedGender = genderSpinner.selectedItem.toString()
        val updatedJobTitle = jobTitleSpinner.selectedItem.toString()

        // Get the current user's UID
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Reference to the Firebase Realtime Database
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

        // Reference to the current user's data
        val userRef: DatabaseReference = databaseReference.child("users").child(userId!!)

        // Create a map to hold the updated user data (excluding email and password)
        val updatedUserData = mapOf(
            "name" to updatedName,
            "phone" to updatedPhone,
            "gender" to updatedGender,
            "jobTitle" to updatedJobTitle
        )

        // Update the user's data in the database
        userRef.updateChildren(updatedUserData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Profile update successful
                    Toast.makeText(
                        requireContext(),
                        "Profile updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Profile update failed
                    Toast.makeText(
                        requireContext(),
                        "Profile update failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}
