package com.example.tempjobv3.data.jobs

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase

class FirebaseDatabaseViewModel : ViewModel() {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Job")

    fun updateJobInFirebase(updatedJob: Jobs) {
        val jobListingReference = databaseReference.child(updatedJob.jobListingId.toString())

        jobListingReference.setValue(updatedJob)
            .addOnSuccessListener {
            }
            .addOnFailureListener { error ->
            }
    }
}