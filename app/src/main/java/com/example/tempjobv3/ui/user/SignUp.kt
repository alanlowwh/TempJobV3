package com.example.tempjobv3.ui.user

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.tempjobv3.R
import com.example.tempjobv3.data.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUp : Fragment() {
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signUpButton = view.findViewById<Button>(R.id.signUpButton)
        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val editTextPhone = view.findViewById<EditText>(R.id.editTextPhone)
        val spinnerGender = view.findViewById<Spinner>(R.id.spinnerGender)
        val spinnerJobTitle = view.findViewById<Spinner>(R.id.spinnerJobTitle)
        val editTextEmail = view.findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = view.findViewById<EditText>(R.id.editTextPassword)
        val loginNowTextView = view.findViewById<TextView>(R.id.loginNow)

        signUpButton.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()
            val gender = spinnerGender.selectedItem.toString().trim()
            val jobTitle = spinnerJobTitle.selectedItem.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Check if the email already exists in 'users' entity
            checkEmailExistence(email, password, name, phone, gender, jobTitle)
        }

        setupSpinner(R.id.spinnerGender, R.array.gender_options)
        setupSpinner(R.id.spinnerJobTitle, R.array.job_title_options)

        loginNowTextView.setOnClickListener {
            // Navigate to the login fragment
            findNavController().navigate(R.id.action_signUp_to_login)
        }
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

    private fun checkEmailExistence(
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: String,
        jobTitle: String
    ) {
        val usersRef: DatabaseReference = databaseReference.child("users")
        val adminRef: DatabaseReference = databaseReference.child("admin")

        // Check if the email exists in 'users' entity
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Email already exists in 'users' entity
                    Toast.makeText(
                        context,
                        "Email already exists. Please choose another email.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Email does not exist in 'users' entity; check in 'admin' entity
                    checkAdminEmailExistence(email, password, name, phone, gender, jobTitle)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(
                    context,
                    "Database error: " + databaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun checkAdminEmailExistence(
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: String,
        jobTitle: String
    ) {
        val adminRef: DatabaseReference = databaseReference.child("admin")

        // Check if the email exists in 'admin' entity
        adminRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Email already exists in 'admin' entity
                    Toast.makeText(
                        context,
                        "Email already exists. Please fill in another email.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Email does not exist in 'admin' entity; create the user account
                    createUserAccount(email, password, name, phone, gender, jobTitle)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(
                    context,
                    "Database error: " + databaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun createUserAccount(
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: String,
        jobTitle: String
    ) {
        // Create a new user account in Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User account created successfully
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    val userId = firebaseUser?.uid

                    // Create a User object
                    val user = User(name, email, phone, gender, password, jobTitle)

                    // Call the registerUser function to save user data to the database
                    registerUser(userId, user)
                } else {
                    // Handle account creation failure, e.g., display an error message
                    Toast.makeText(context, "Account creation failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser(userId: String?, user: User) {
        // Save the user data to Firebase Realtime Database
        if (userId != null) {
            databaseReference.child("users").child(userId).setValue(user)
                .addOnCompleteListener { databaseTask ->
                    if (databaseTask.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Account creation successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_signUp_to_login)
                    } else {
                        // Handle database write failure
                        Toast.makeText(
                            context,
                            "Failed to save user data to the database",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            // Handle user ID is null
            Toast.makeText(context, "User ID is null", Toast.LENGTH_SHORT).show()
        }
    }
}
