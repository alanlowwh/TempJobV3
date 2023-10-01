package com.example.tempjobv3.data.jobs

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase

class FirebaseDatabaseViewModel : ViewModel() {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Job")

    fun updateJobInFirebase(updatedJob: Jobs) {
        val jobListingReference = databaseReference.child(updatedJob.jobListingId.toString())

        jobListingReference.setValue(updatedJob)
            .addOnSuccessListener {
                // Handle success (the data has been successfully updated)
                // You can notify the user or perform any other necessary action here
            }
            .addOnFailureListener { error ->
                // Handle failure (an error occurred while updating the data)
                // You can log the error or show an error message to the user
            }
    }
}