//package com.example.tempjobv3.ui.user
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.tempjobv3.R
//import com.example.tempjobv3.data.jobs.Jobs
//import com.google.firebase.database.*
//
//class CusJobList : Fragment() {
//
//    private lateinit var jobListAdapter: CusJobListAdapter
//    private lateinit var jobList: MutableList<Jobs>
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var databaseReference: DatabaseReference
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_cus_job_list, container, false)
//
//        recyclerView = view.findViewById(R.id.recyclerviewJobList)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        jobList = mutableListOf()
//        jobListAdapter = CusJobListAdapter(jobList)
//        recyclerView.adapter = jobListAdapter
//
//        // Assuming you have a reference to your Firebase database
//        val databaseReference = FirebaseDatabase.getInstance().getReference("Job")
//
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    for (jobSnapshot in snapshot.children) {
//                        val jobListingIdLong = jobSnapshot.child("jobListingId").getValue(Long::class.java)
//                        val jobTitle = jobSnapshot.child("jobTitle").getValue(String::class.java)
//                        val companyName = jobSnapshot.child("companyName").getValue(String::class.java)
//                        val companyAddress = jobSnapshot.child("companyAddress").getValue(String::class.java)
//
//                        // Check if jobListingIdLong is not null before conversion
//                        if (jobListingIdLong != null && jobTitle != null && companyName != null && companyAddress != null) {
//                            // Convert jobListingIdLong to a String if needed
//                            val jobListingId = jobListingIdLong.toString()
//
//                            val job = Jobs(jobListingId, jobTitle, companyName)
//                            // Process the 'job' object as needed
//                            // You can add 'job' to a list or update your UI with the data
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle the error
//            }
//        })
//        return view
//    }
//}
