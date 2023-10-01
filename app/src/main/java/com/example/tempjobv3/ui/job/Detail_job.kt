package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.Jobs
import com.example.tempjobv3.data.user.JobApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class detail_job : Fragment() {

    private lateinit var receivedJob: Jobs
    private var currentUser: FirebaseUser? = null
    private lateinit var databaseReference: DatabaseReference
    private val email: String? = FirebaseAuth.getInstance().currentUser?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val args = detail_jobArgs.fromBundle(it)
            receivedJob = args.selectedJobToDetail
        }

        currentUser = FirebaseAuth.getInstance().currentUser

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference

        Log.d("ReceiveJob", receivedJob.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_job, container, false)

        if (receivedJob != null) {
            val jobTitle = view.findViewById<TextView>(R.id.detail_jobTitleTextView)
            val companyName = view.findViewById<TextView>(R.id.detail_companyNameTextView)
            val salary = view.findViewById<TextView>(R.id.detail_salary)
            val workplaceTypeEditText = view.findViewById<TextView>(R.id.detail_workplaceType)
            val jobLocationEditText = view.findViewById<TextView>(R.id.detail_jobLocation)
            val jobTypeEditText = view.findViewById<TextView>(R.id.detail_jobType)
            val jobDescriptionEditText = view.findViewById<TextView>(R.id.detail_jobDescription)
            val datePosted = view.findViewById<TextView>(R.id.detail_datePosted)

            jobTitle.text = receivedJob.jobTitle
            companyName.text = receivedJob.companyName
            salary.text = receivedJob.salary.toString()
            workplaceTypeEditText.text = receivedJob.workplaceType
            jobLocationEditText.text = receivedJob.jobLocation
            jobTypeEditText.text = receivedJob.jobType
            jobDescriptionEditText.text = receivedJob.jobDescription
            datePosted.text = receivedJob.datePosted

            val viewJobApplicationButton = view.findViewById<Button>(R.id.viewJobApplicationButton)

            // Check user role by querying Firebase Realtime Database
            if (email != null) {
                val usersRef: DatabaseReference = databaseReference.child("users")

                usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val user = dataSnapshot.children.firstOrNull()
                            val role = user?.child("role")?.value as? String

                            if (role == "admin") {
                                // User has the 'admin' role, customize the admin action
                                viewJobApplicationButton.text = "View Job Application"
                                viewJobApplicationButton.setOnClickListener {
                                    // Navigate to the Application Job page for admin
                                    val action =
                                        detail_jobDirections.actionDetailJobToApplicationJob()
                                    findNavController().navigate(action)
                                }
                            } else {
                                // User has the 'customer' role, customize the customer action
                                viewJobApplicationButton.text = "Apply for Job"
                                viewJobApplicationButton.setOnClickListener {
                                    applyForJob()
                                }
                            }
                        } else {
                            // User not found in 'users' entity
                            // Handle this case accordingly
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                    }
                })
            }
        }

        return view
    }

    private fun applyForJob() {
        val jobApplicationId = generateUniqueJobApplicationId()
        val customerId = currentUser?.uid
        val status = "Pending"
        val jobApplicationDate = getCurrentDate()

        val jobApplication = JobApplication(
            jobApplicationId,
            receivedJob.jobReferences.toString(), // Ensure jobListingId is a String
            customerId ?: "",
            status,
            jobApplicationDate
        )

        val jobApplicationsRef = databaseReference.child("jobApplications")

        jobApplicationsRef.child(jobApplicationId).setValue(jobApplication)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Application submitted successfully
                    Toast.makeText(requireContext(), "Application submitted!", Toast.LENGTH_LONG).show()
                    // You can navigate to another screen here if needed
                } else {
                    // Application submission failed, handle the error
                    Toast.makeText(requireContext(), "Failed to submit application", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun generateUniqueJobApplicationId(): String {
        // Generate a unique job application ID using a suitable method (e.g., UUID)
        return UUID.randomUUID().toString()
    }

    private fun getCurrentDate(): String {
        // Get the current date in your desired format
        val currentDate = SimpleDateFormat("dd/MM/yyyy").format(Date())
        return currentDate
    }
}
