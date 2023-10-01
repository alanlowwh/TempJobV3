package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.AddJobViewModel
import com.example.tempjobv3.data.jobs.Jobs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class delete_job : Fragment() {

    private lateinit var receivedJob: Jobs
    private lateinit var addJobViewModel: AddJobViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addJobViewModel = ViewModelProvider(this).get(AddJobViewModel::class.java)

        // Use Safe Args to retrieve the passed argument
        arguments?.let {
            val args = delete_jobArgs.fromBundle(it)
            receivedJob = args.selectedDeleteJob
        }

        // Launch a coroutine to perform the deletion
        viewLifecycleOwner.lifecycleScope.launch {
            // Remove the job locally
            addJobViewModel.deleteJobs(receivedJob)

            // Remove the job from Firebase
            val databaseReference = FirebaseDatabase.getInstance().getReference("Job")
                .child(receivedJob.jobReferences.toString())

            databaseReference.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Deletion from Firebase was successful
                    Toast.makeText(
                        requireContext(),
                        "Successfully deleted!",
                        Toast.LENGTH_LONG
                    ).show()
                    // Navigate back to the list fragment screen
                    findNavController().navigate(R.id.action_delete_job_to_list_job)
                } else {
                    // Handle any errors during Firebase deletion
                    Toast.makeText(
                        requireContext(),
                        "Failed to delete job from Firebase",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
