package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.AddJobViewModel
import com.example.tempjobv3.data.jobs.Jobs


class edit_job : Fragment() {

    private lateinit var receivedJob: Jobs
    private lateinit var addJobViewModel: AddJobViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addJobViewModel = ViewModelProvider(this).get(AddJobViewModel::class.java)


        // Use Safe Args to retrieve the passed argument
        arguments?.let {
            val args = edit_jobArgs.fromBundle(it)
            receivedJob = args.selectedJob
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_job, container, false)

        // Check if a Jobs object was received
        if (receivedJob != null) {
            // Find the EditText fields in your layout
            val jobTitleEditText = view.findViewById<EditText>(R.id.update_jobTitle_editText)
            val companyNameEditText = view.findViewById<EditText>(R.id.update_companyName_editText)
//            val workplaceTypeEditText = view.findViewById<EditText>(R.id.update_workplaceType_editText)
//            val jobLocationEditText = view.findViewById<EditText>(R.id.update_jobLocation_editText)
//            val jobTypeEditText = view.findViewById<EditText>(R.id.update_jobType_editText)
//            val jobDescriptionEditText = view.findViewById<EditText>(R.id.update_jobDescription_editText)
            val salaryEditText = view.findViewById<EditText>(R.id.update_salary_editText)

            jobTitleEditText.setText(receivedJob.jobTitle)
            companyNameEditText.setText(receivedJob.companyName)
//            workplaceTypeEditText.setText(receivedJob.workplaceType)
//            jobLocationEditText.setText(receivedJob.jobLocation)
//            jobTypeEditText.setText(receivedJob.jobType)
//            jobDescriptionEditText.setText(receivedJob.jobDescription)
            salaryEditText.setText(receivedJob.salary.toString())




            val updateButton = view.findViewById<Button>(R.id.update_job_btn)

            // Set a click listener for the update button
            updateButton.setOnClickListener {
                updateJobDetails()
            }

        }

        return view
    }

    private fun updateJobDetails() {
        // Retrieve the edited values from the EditText fields
        val jobTitleEditText = jobTitleEditText.text.toString()
        val updatedCompanyName = companyNameEditText.text.toString()
        val updatedSalary = salaryEditText.text.toString().toDouble()

        // Create a new Jobs object with the updated values
        val updatedJob = Jobs(
            receivedJob.id, // Keep the same ID
            updatedJobTitle,
            updatedCompanyName,
            receivedJob.workplaceType,
            receivedJob.jobLocation,
            receivedJob.jobType,
            receivedJob.jobDescription,
            updatedSalary
        )

        // Update the job details in the ViewModel
        addJobViewModel.updateJobs(updatedJob)

        // Navigate back to the job list or perform any other necessary action
        findNavController().navigateUp()
    }


}