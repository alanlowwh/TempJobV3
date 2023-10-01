package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.AddJobViewModel
import com.example.tempjobv3.data.jobs.Jobs
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class edit_job : Fragment() {
    private lateinit var receivedJob: Jobs
    private lateinit var updateButton: Button
    private lateinit var jobTitleEditText: EditText
    private lateinit var companyNameEditText: EditText
    private lateinit var workplaceTypeSpinner: Spinner
    private lateinit var jobLocationEditText: EditText
    private lateinit var jobTypeEditText: EditText
    private lateinit var jobDescriptionEditText: EditText
    private lateinit var salaryEditText: EditText
    private lateinit var jobReferences: String // Initialize this property
    private lateinit var addJobViewModel: AddJobViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addJobViewModel = ViewModelProvider(this).get(AddJobViewModel::class.java)

        arguments?.let {
            val args = edit_jobArgs.fromBundle(it)
            receivedJob = args.selectedJob
            jobReferences = receivedJob.jobReferences
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_job, container, false)

        // Initialize UI elements
        updateButton = view.findViewById(R.id.update_job_btn)
        jobTitleEditText = view.findViewById(R.id.update_jobTitle_editText)
        companyNameEditText = view.findViewById(R.id.update_companyName_editText)
        workplaceTypeSpinner = view.findViewById(R.id.update_spinner_workplacetype)
        jobLocationEditText = view.findViewById(R.id.update_jobLocation_editText)
        jobTypeEditText = view.findViewById(R.id.update_jobType_editText)
        jobDescriptionEditText = view.findViewById(R.id.update_jobDescription_editText)
        salaryEditText = view.findViewById(R.id.update_salary_editText)

        // Set UI elements with received job data
        jobTitleEditText.setText(receivedJob.jobTitle)
        companyNameEditText.setText(receivedJob.companyName)
        jobLocationEditText.setText(receivedJob.jobLocation)
        jobTypeEditText.setText(receivedJob.jobType)
        jobDescriptionEditText.setText(receivedJob.jobDescription)
        salaryEditText.setText(receivedJob.salary.toString())

        // Initialize the spinner with received data
        val workplaceTypes = resources.getStringArray(R.array.spinner_items)
        val workplaceTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            workplaceTypes
        )
        workplaceTypeSpinner.adapter = workplaceTypeAdapter
        val selectedWorkplaceType = receivedJob.workplaceType
        val selectedWorkplaceTypePosition = workplaceTypes.indexOf(selectedWorkplaceType)
        workplaceTypeSpinner.setSelection(selectedWorkplaceTypePosition)

        // Add an OnClickListener to the "Update Job" button
        updateButton.setOnClickListener {
            updateJob()
        }

        return view
    }

    private fun updateJob() {
        val editedJobTitle = jobTitleEditText.text.toString()
        val editedCompanyName = companyNameEditText.text.toString()
        val editedWorkplaceType = workplaceTypeSpinner.selectedItem.toString()
        val editedJobLocation = jobLocationEditText.text.toString()
        val editedJobType = jobTypeEditText.text.toString()
        val editedJobDescription = jobDescriptionEditText.text.toString()
        val editedSalary = salaryEditText.text.toString()

        if (inputCheck(
                editedJobTitle,
                editedCompanyName,
                editedSalary,
                editedJobDescription,
                editedWorkplaceType,
                editedJobType,
                editedJobLocation
            )
        ) {
            // Update job in local database
            val updatedJob = Jobs(
                receivedJob.jobListingId,
                receivedJob.jobReferences,
                editedJobTitle,
                editedCompanyName,
                editedSalary.toInt(),
                editedJobDescription,
                editedWorkplaceType,
                editedJobType,
                receivedJob.datePosted,
                receivedJob.jobListingStatus,
                editedJobLocation
            )

            addJobViewModel.updateJobs(updatedJob)

            // Update job in Firebase Realtime Database
            val databaseReference: DatabaseReference =
                Firebase.database.reference.child("Job").child(jobReferences)

            val updatedJobData = mapOf(
                "jobTitle" to editedJobTitle,
                "companyName" to editedCompanyName,
                "workplaceType" to editedWorkplaceType,
                "jobLocation" to editedJobLocation,
                "jobType" to editedJobType,
                "jobDescription" to editedJobDescription,
                "salary" to editedSalary
            )

            databaseReference.updateChildren(updatedJobData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully updated!",
                            Toast.LENGTH_LONG
                        ).show()

                        // Navigate back to list fragment screen
                        findNavController().navigate(R.id.action_edit_job_to_list_job)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to update job listing",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill out all fields.",
                Toast.LENGTH_LONG
            ).show()
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
        return !(TextUtils.isEmpty(jobTitle) || TextUtils.isEmpty(companyName)
                || TextUtils.isEmpty(salary) || TextUtils.isEmpty(jobDescription)
                || TextUtils.isEmpty(workplaceType) || TextUtils.isEmpty(jobType)
                || TextUtils.isEmpty(jobLocation))
    }
}
