package com.example.tempjobv3.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tempjobv3.R
import com.example.tempjobv3.data.user.JobApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AppliedJobs : Fragment() {

    private lateinit var jobApplicationsAdapter: AppliedJobsAdapter
    private lateinit var jobApplicationsList: MutableList<JobApplication>
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_applied_jobs, container, false)
        recyclerView = rootView.findViewById(R.id.applied_jobs_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        jobApplicationsList = mutableListOf()
        jobApplicationsAdapter = AppliedJobsAdapter(jobApplicationsList)
        recyclerView.adapter = jobApplicationsAdapter

        // Initialize Firebase Database reference for your job applications
        databaseReference = FirebaseDatabase.getInstance().reference

        // Add a ValueEventListener to fetch data from Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                jobApplicationsList.clear()
                val jobApplicationsSnapshot = dataSnapshot.child("jobApplications")
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

                for (postSnapshot in jobApplicationsSnapshot.children) {
                    try {
                        val jobApplication = postSnapshot.getValue(JobApplication::class.java)
                        if (jobApplication != null && jobApplication.customerId == currentUserId) {
                            // Fetch job details based on jobId
                            val jobId = jobApplication.jobReferences
                            val jobDetailsSnapshot = dataSnapshot.child("Job").child(jobId)
                            val jobTitle = jobDetailsSnapshot.child("jobTitle").getValue(String::class.java)
                            val companyName = jobDetailsSnapshot.child("companyName").getValue(String::class.java)
                            val companyAddress = jobDetailsSnapshot.child("jobLocation").getValue(String::class.java)

                            // Set job details in the JobApplication object
                            jobApplication.jobTitle = jobTitle
                            jobApplication.companyName = companyName
                            jobApplication.jobLocation = companyAddress

                            jobApplicationsList.add(jobApplication)
                        }
                    } catch (e: Exception) {
                        Log.e("AppliedJobs", "Error parsing data: ${e.message}")
                    }
                }
                jobApplicationsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        return rootView
    }
}
