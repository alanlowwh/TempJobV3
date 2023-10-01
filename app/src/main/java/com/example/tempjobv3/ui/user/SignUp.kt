package com.example.tempjobv3.ui.user

import android.app.ProgressDialog
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.PatternsCompat
import androidx.navigation.fragment.findNavController
import com.example.tempjobv3.R
import com.example.tempjobv3.data.user.User
import com.google.android.gms.tasks.Continuation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference

class SignUp : Fragment(R.layout.fragment_sign_up) {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var spinnerJobTitle: Spinner
    private var resumeUri: Uri? = null
    private var isUploading = false // Flag to track upload progress

    private lateinit var databaseReference: DatabaseReference
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        editTextName = view.findViewById(R.id.editTextName)
        editTextPhone = view.findViewById(R.id.editTextPhone)
        spinnerGender = view.findViewById(R.id.spinnerGender)
        spinnerJobTitle = view.findViewById(R.id.spinnerJobTitle)
        signUpButton = view.findViewById<Button>(R.id.signUpButton)

        val uploadResumeButton = view.findViewById<Button>(R.id.buttonResumeUpload)

        uploadResumeButton.setOnClickListener {
            if (!isUploading) { // Check if upload is not in progress
                val galleryIntent = Intent()
                galleryIntent.action = Intent.ACTION_GET_CONTENT
                galleryIntent.type = "application/pdf"
                startActivityForResult(galleryIntent, 2)
            }
        }

        signUpButton.setOnClickListener {
            if (!isUploading) { // Check if upload is not in progress
                val name = editTextName.text.toString().trim()
                val email = editTextEmail.text.toString().trim()
                val password = editTextPassword.text.toString().trim()
                val phone = editTextPhone.text.toString().trim()

                // Perform validation checks in the specified order
                val errorMessages = mutableListOf<String>()

                if (name.isEmpty()) {
                    errorMessages.add("Name cannot be empty.")
                }else if(!isValidName(name)){
                    errorMessages.add("Invalid Name.")
                }

                if (email.isEmpty()) {
                    errorMessages.add("Email cannot be empty.")
                } else if (!isValidEmail(email)) {
                    errorMessages.add("Invalid email address.")
                }

                if (password.isEmpty()) {
                    errorMessages.add("Password cannot be empty.")
                } else if (!isValidPassword(password)) {
                    errorMessages.add("Invalid password. Password should be at least 6 characters long and contain at least one symbol.")
                }

                if (phone.isEmpty()) {
                    errorMessages.add("Phone number cannot be empty.")
                } else if (!isValidPhone(phone)) {
                    errorMessages.add("Invalid phone number. Phone should start from 0 and be 10-11 digits.")
                }

                if (resumeUri == null) {
                    errorMessages.add("Please select a resume file.")
                }

                if (errorMessages.isEmpty()) {
                    checkEmailExistence(email, password, name, phone, spinnerGender.selectedItem.toString(), spinnerJobTitle.selectedItem.toString())
                } else {
                    // Show error messages one by one
                    for (errorMessage in errorMessages) {
                        showSnackbar(errorMessage)
                    }
                }
            }
        }

        setupSpinner(R.id.spinnerGender, R.array.gender_options)
        setupSpinner(R.id.spinnerJobTitle, R.array.job_title_options)

        val loginNowTextView = view.findViewById<TextView>(R.id.loginNow)

        loginNowTextView.setOnClickListener {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            resumeUri = data.data
            showSnackbar("Resume uploaded successfully")
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        // Validate phone with a regular expression for 10-11 digit numbers starting with '0'
        val phonePattern = Regex("^0[0-9]{9,10}\$")
        return phonePattern.matches(phone)
    }

    private fun isValidEmail(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        // Validate password with a regular expression for at least 6 characters and at least one symbol
        val passwordPattern = Regex("^(?=.*[A-Za-z0-9])(?=.*[^A-Za-z0-9]).{6,}\$")
        return passwordPattern.matches(password)
    }

    private fun isValidName(name: String): Boolean {
        val namePattern = Regex("^[a-zA-Z ]+\$") // Allows alphabetic characters and spaces
        return namePattern.matches(name)
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

        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    showSnackbar("Email already exists. Please choose another email.")
                } else {
                    checkAdminEmailExistence(email, password, name, phone, gender, jobTitle)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showSnackbar("Database error: ${databaseError.message}")
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

        adminRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    showSnackbar("Email already exists. Please fill in another email.")
                } else {
                    signUpWithResume(
                        email,
                        password,
                        name,
                        phone,
                        gender,
                        jobTitle
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showSnackbar("Database error: ${databaseError.message}")
            }
        })
    }

    private fun signUpWithResume(
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: String,
        jobTitle: String
    ) {
        isUploading = true // Set the flag to indicate that the upload has started
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading...")
        progressDialog.setCancelable(false) // Prevent users from dismissing the dialog
        progressDialog.show()

        val fileName = "${System.currentTimeMillis()}_${resumeUri?.lastPathSegment}"
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("resumes/$fileName")

        val uploadTask = storageRef.putFile(resumeUri!!)

        uploadTask.continueWithTask(Continuation { task ->
            if (!task.isSuccessful) {
                // Handle upload failure and show appropriate error messages
                // ...
                isUploading = false // Ensure the flag is set to false on failure
            }
            return@Continuation storageRef.downloadUrl
        }).addOnCompleteListener { task ->
            progressDialog.dismiss() // Dismiss the progress dialog when the task completes
            isUploading = false // Set the flag to indicate that the upload has completed

            if (task.isSuccessful) {
                val downloadUri = task.result
                if (downloadUri != null) {
                    val resumeUrl = downloadUri.toString()
                    createUserAccount(email, password, name, phone, gender, jobTitle, resumeUrl)
                } else {
                    showSnackbar("Failed to get resume URL")
                }
            } else {
                // Handle upload failure and show appropriate error messages
                // ...
            }
        }
    }

    private fun createUserAccount(
        email: String,
        password: String,
        name: String,
        phone: String,
        gender: String,
        jobTitle: String,
        resumeUrl: String
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    val userId = firebaseUser?.uid

                    val user = User(name, email, phone, gender, password, jobTitle, "customer", resumeUrl)

                    saveUserDataToDatabase(userId, user)
                } else {
                    showSnackbar("Account creation failed")
                }
            }
    }

    private fun saveUserDataToDatabase(userId: String?, user: User) {
        if (userId != null) {
            databaseReference.child("users").child(userId).setValue(user)
                .addOnCompleteListener { databaseTask ->
                    if (databaseTask.isSuccessful) {
                        showSnackbar("Account creation successful")
                        findNavController().navigate(R.id.action_signUp_to_login)
                    } else {
                        showSnackbar("Failed to save user data to the database")
                    }
                }
        } else {
            showSnackbar("Invalid user ID")
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }
}
