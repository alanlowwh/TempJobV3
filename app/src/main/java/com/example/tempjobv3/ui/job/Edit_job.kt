package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.withContext

import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.AddJobViewModel
import com.example.tempjobv3.data.jobs.Jobs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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


            // Set a click listener for the update button

            val updateButton = view.findViewById<Button>(R.id.update_job_btn)

            updateButton.setOnClickListener {

                val EditedJobTitleEditText = view.findViewById<EditText>(R.id.update_jobTitle_editText).text
                val EditedCompanyNameEditText = view.findViewById<EditText>(R.id.update_companyName_editText).text
//            val workplaceTypeEditText = view.findViewById<EditText>(R.id.update_workplaceType_editText)
//            val jobLocationEditText = view.findViewById<EditText>(R.id.update_jobLocation_editText)
//            val jobTypeEditText = view.findViewById<EditText>(R.id.update_jobType_editText)
//            val jobDescriptionEditText = view.findViewById<EditText>(R.id.update_jobDescription_editText)
                val EditedSalaryEditText = view.findViewById<EditText>(R.id.update_salary_editText).text

                if (inputCheck(
                        EditedJobTitleEditText.toString(),
                        EditedCompanyNameEditText.toString(),
                        EditedSalaryEditText.toString())
                ) {
                    // Create a new Jobs object with the updated values
                    val updatedJob = Jobs(
                        receivedJob.jobListingId, // Keep the same ID
                        EditedJobTitleEditText.toString(),
                        EditedCompanyNameEditText.toString(),
//                    receivedJob.workplaceType,
//                    receivedJob.jobLocation,
//                    receivedJob.jobType,
//                    receivedJob.jobDescription,
                        EditedSalaryEditText.toString().toInt()
                    )

                    // Update the job details in the ViewModel
//                    addJobViewModel.updateJobs(updatedJob)

                    // Navigate back to the job list or perform any other necessary action
                    addJobViewModel.viewModelScope.launch(Dispatchers.IO) {
                        //Add to Database
                        addJobViewModel.updateJobs(updatedJob)
                        withContext(Dispatchers.Main) {
                            // Show success message and navigate to another fragment
                            Toast.makeText(
                                requireContext(),
                                "Successfully updated!",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            //Navigate back to list fragment screen
                            findNavController().navigate(R.id.action_edit_job_to_list_job)

                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG)
                        .show()

                }


            }

        }

        return view
    }

    private fun inputCheck(jobTitle: String, companyName: String, salary: String): Boolean {
        return !(TextUtils.isEmpty(jobTitle) || TextUtils.isEmpty(companyName) || TextUtils.isEmpty(
            salary
        ))
    }


}