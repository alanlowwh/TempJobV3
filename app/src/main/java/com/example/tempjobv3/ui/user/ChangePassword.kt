package com.example.tempjobv3.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tempjobv3.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChangePassword : Fragment() {

    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var updatePasswordButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        // Initialize UI elements
        currentPasswordEditText = view.findViewById(R.id.currentPassword)
        newPasswordEditText = view.findViewById(R.id.newPassword)
        confirmPasswordEditText = view.findViewById(R.id.confirmPassword)
        updatePasswordButton = view.findViewById(R.id.updatePasswordButton)

        // Add an OnClickListener to the "Update Password" button
        updatePasswordButton.setOnClickListener {
            // Call the function to update the password
            updatePassword()
        }
        return view
    }

    private fun updatePassword() {
        // Get the current user
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Get the current password entered by the user
        val currentPassword = currentPasswordEditText.text.toString()

        // Get the new password and confirm password entered by the user
        val newPassword = newPasswordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        // Check that the current password matches the Firebase password
        val credential = EmailAuthProvider.getCredential(currentUser?.email!!, currentPassword)
        currentUser.reauthenticate(credential)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    // Password verification successful
                    // Now, check if the new password and confirm password match
                    if (newPassword == confirmPassword) {
                        // Update the password
                        currentUser.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    // Password update successful
                                    Toast.makeText(
                                        requireContext(),
                                        "Password updated successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Update the password field in the Realtime Database
                                    updatePasswordInDatabase(newPassword)
                                } else {
                                    // Password update failed
                                    Toast.makeText(
                                        requireContext(),
                                        "Password update failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        // New password and confirm password do not match
                        Toast.makeText(
                            requireContext(),
                            "New password and confirm password do not match",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Password verification failed
                    Toast.makeText(
                        requireContext(),
                        "Current password verification failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updatePasswordInDatabase(newPassword: String) {
        // Get the current user's UID
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Reference to the Firebase Realtime Database
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

        // Reference to the current user's data
        val userRef: DatabaseReference = databaseReference.child("users").child(userId!!)

        // Update the password field in the database
        userRef.child("password").setValue(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password update in the database successful
                } else {
                    // Password update in the database failed
                }
            }
    }
}
