package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.example.tempjobv3.data.jobs.JobsDatabase
import com.example.tempjobv3.data.jobs.JobsRepository
import com.example.tempjobv3.databinding.FragmentAddJobBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class add_job : Fragment() {

    private lateinit var mAddJobViewModel: AddJobViewModel
    private lateinit var binding: FragmentAddJobBinding
    private lateinit var jobRepo: JobsRepository

    private var shouldAddLocally = false
    private var shouldAddToFirebase = false

    private var idRef = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddJobBinding.inflate(inflater, container, false)
        val view = binding.root

        mAddJobViewModel = ViewModelProvider(this).get(AddJobViewModel::class.java)

        val jobsDao = JobsDatabase.getDatabase(requireContext()).jobsDao()
        jobRepo = JobsRepository(jobsDao)

        binding.viewModel = mAddJobViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.postJobBtn.setOnClickListener {
            // Determine where to add the job based on user input
            shouldAddLocally = true // Set to true if you want to add locally
            shouldAddToFirebase = true // Set to true if you want to add to Firebase
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val jobTitle = binding.jobTitleEditText.text.toString()
        val companyName = binding.companyNameEditText.text.toString()
        val salaryText = binding.salaryEditText.text.toString()
        val jobDescription = binding.jobDescriptionEditText.text.toString()
        val jobType = binding.jobTypeEditTexts.text.toString()
        val jobLocation = binding.jobLocationEditText.text.toString()
        val workplaceType = binding.spinnerWorkplacetype.selectedItem.toString()

        if (inputCheck(
                jobTitle,
                companyName,
                salaryText,
                jobDescription,
                workplaceType,
                jobType,
                jobLocation
            )
        ) {
            val currentDate = getCurrentDate()
            val formattedDate = formatDate(currentDate)

                if (shouldAddToFirebase) {
                    // Add to Firebase Realtime Database
                    val FirebaseJob = Jobs(
                        jobTitle = jobTitle,
                        companyName = companyName,
                        salary = salaryText.toInt(),
                        jobDescription = jobDescription,
                        workplaceType = workplaceType,
                        jobType = jobType,
                        datePosted = formattedDate,
                        jobListingStatus = "Available",
                        jobLocation = jobLocation,
                        jobReferences =""
                    )

                    val database = FirebaseDatabase.getInstance()
                    val reference = database.getReference("Job")
                    val newEntryReference = reference.push()
                    idRef = newEntryReference.key.toString()
                    FirebaseJob.jobReferences = newEntryReference.key.toString()
                    newEntryReference.setValue(FirebaseJob)

                    Log.d("please", FirebaseJob.toString())

                }

            val job = Jobs(
                jobTitle = jobTitle,
                companyName = companyName,
                salary = salaryText.toInt(),
                jobDescription = jobDescription,
                workplaceType = workplaceType,
                jobType = jobType,
                datePosted = formattedDate,
                jobListingStatus = "Available",
                jobLocation = jobLocation,
                jobReferences = idRef
            )

            mAddJobViewModel.viewModelScope.launch(Dispatchers.IO) {
                if (shouldAddLocally) {
                    // Add to the local database
                    mAddJobViewModel.addJobs(job)
                }

                Log.d("please", job.toString())

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Successfully posted!", Toast.LENGTH_LONG)
                        .show()
                    findNavController().navigate(R.id.action_add_job_to_list_job)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun inputCheck(
        jobTitle: String,
        companyName: String,
        salary: String,
        jobDescription: String,
        workplaceType: String,
        jobType: String,
        jobLocation: String
    ): Boolean {
        return !(TextUtils.isEmpty(jobTitle) || TextUtils.isEmpty(companyName) || TextUtils.isEmpty(
            salary
        ) || TextUtils.isEmpty(jobDescription) || TextUtils.isEmpty(workplaceType) || TextUtils.isEmpty(
            jobType
        ) || TextUtils.isEmpty(jobLocation))
    }

    fun getCurrentDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    fun formatDate(date: Date?): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        return sdf.format(date)
    }
}

