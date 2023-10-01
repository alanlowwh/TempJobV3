package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tempjobv3.R
import com.example.tempjobv3.data.user.JobApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ApplicationJob : Fragment() {

    private var currentUser: FirebaseUser? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var applicationJobAdapter: ApplicationJobAdapter
    private val appliedJobsList = mutableListOf<JobApplication>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use ?. to safely get the currentUser
        currentUser = FirebaseAuth.getInstance().currentUser

        // Check if currentUser is null
        if (currentUser == null) {
            // Handle the case where the user is not authenticated
            // You can show a message or redirect to the login screen
            // For example:
            // startActivity(Intent(context, LoginActivity::class.java))
            // activity?.finish()
        } else {
            // Proceed with database operations
            databaseReference = FirebaseDatabase.getInstance().reference.child("jobApplications")

            // Initialize RecyclerView and set the adapter
            applicationJobAdapter = ApplicationJobAdapter(appliedJobsList)

            // Retrieve applied job data from Firebase and populate the list
            retrieveAppliedJobs()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_application_job, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.application_job)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = applicationJobAdapter

        return view
    }

    private fun retrieveAppliedJobs() {
        // Check if currentUser is null before proceeding
        currentUser?.let { user ->
            // Define the database query here
            val query = databaseReference.orderByChild("customerId").equalTo(user.uid)

            query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    appliedJobsList.clear()

                    for (jobApplicationSnapshot in dataSnapshot.children) {
                        val jobApplication = jobApplicationSnapshot.getValue(JobApplication::class.java)
                        jobApplication?.let {
                            if (it.jobApplicationId == "c45eb20b-dd97-487e-85cb-ef06f46f1416") {
                                appliedJobsList.add(it)
                            }
                        }
                    }

                    applicationJobAdapter.notifyDataSetChanged()

                    // Log the retrieved data
                    Log.d("ApplicationJob", "Retrieved ${appliedJobsList.size} job applications")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error and log it
                    Log.e("ApplicationJob", "Database error: ${databaseError.message}")
                }
            })
        }
    }

}
