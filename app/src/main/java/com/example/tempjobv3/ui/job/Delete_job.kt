package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController

import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.AddJobViewModel
import com.example.tempjobv3.data.jobs.Jobs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [delete_job.newInstance] factory method to
 * create an instance of this fragment.
 */
class delete_job : Fragment() {

    private lateinit var receivedJob: Jobs
    private lateinit var addJobViewModel: AddJobViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addJobViewModel = ViewModelProvider(this).get(AddJobViewModel::class.java)


        // Use Safe Args to retrieve the passed argument
        arguments?.let {
            val args = delete_jobArgs.fromBundle(it)
            receivedJob = args.selectedDeleteJob
        }

        addJobViewModel.viewModelScope.launch(Dispatchers.IO) {
            //Add to Database
            addJobViewModel.deleteJobs(receivedJob)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(), "Successfully deleted!", Toast.LENGTH_LONG
                ).show()
                //Navigate back to list fragment screen
                findNavController().navigate(R.id.action_delete_job_to_list_job)

            }
//Not working
//            val databaseReference = FirebaseDatabase.getInstance().getReference("Job").child(receivedJob.jobListingId.toString())
//            databaseReference.removeValue()




        }


    }


}