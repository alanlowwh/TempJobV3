package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_job, container, false)

        // Check if a Jobs object was received
        if (receivedJob != null) {
            // Find the EditText fields layout
            val jobTitleEditText = view.findViewById<EditText>(R.id.update_jobTitle_editText)
            val companyNameEditText = view.findViewById<EditText>(R.id.update_companyName_editText)
            val salaryEditText = view.findViewById<EditText>(R.id.update_salary_editText)

            val workplaceTypeSpinner =
                view.findViewById<Spinner>(R.id.update_spinner_workplacetype)
            val jobLocationEditText = view.findViewById<EditText>(R.id.update_jobLocation_editText)
            val jobTypeEditText = view.findViewById<EditText>(R.id.update_jobType_editText)
            val jobDescriptionEditText =
                view.findViewById<EditText>(R.id.update_jobDescription_editText)


            jobTitleEditText.setText(receivedJob.jobTitle)
            companyNameEditText.setText(receivedJob.companyName)

            when(receivedJob.workplaceType){
                "On-site" -> {
                    workplaceTypeSpinner.setSelection(0)
                }
                "Remote" -> {
                    workplaceTypeSpinner.setSelection(1)
                }
                "Hybrid" -> {
                    workplaceTypeSpinner.setSelection(2)

                }
            }


            jobLocationEditText.setText(receivedJob.jobLocation)
            jobTypeEditText.setText(receivedJob.jobType)
            jobDescriptionEditText.setText(receivedJob.jobDescription)
            salaryEditText.setText(receivedJob.salary.toString())


            // Set a click listener for the update button

            val updateButton = view.findViewById<Button>(R.id.update_job_btn)

            updateButton.setOnClickListener {

                val EditedJobTitleEditText =
                    view.findViewById<EditText>(R.id.update_jobTitle_editText).text
                val EditedCompanyNameEditText =
                    view.findViewById<EditText>(R.id.update_companyName_editText).text
                val workplaceTypeSpinner =
                    view.findViewById<Spinner>(R.id.update_spinner_workplacetype).selectedItem.toString()
                val jobLocationEditText =
                    view.findViewById<EditText>(R.id.update_jobLocation_editText).text
                val jobTypeEditText = view.findViewById<EditText>(R.id.update_jobType_editText).text
                val jobDescriptionEditText =
                    view.findViewById<EditText>(R.id.update_jobDescription_editText).text
                val EditedSalaryEditText =
                    view.findViewById<EditText>(R.id.update_salary_editText).text


                if (inputCheck(
                        EditedJobTitleEditText.toString(),
                        EditedCompanyNameEditText.toString(),
                        EditedSalaryEditText.toString(),
                        jobDescriptionEditText.toString(),
                        workplaceTypeSpinner.toString(),
                        jobTypeEditText.toString(),
                        jobLocationEditText.toString()
                    )
                ) {

                    // Create a new Jobs object with the updated values
                    val updatedJob = Jobs(
                        receivedJob.jobListingId, // Keep the same ID

                        EditedJobTitleEditText.toString(),
                        EditedCompanyNameEditText.toString(),
                        EditedSalaryEditText.toString().toInt(),
                        jobDescriptionEditText.toString(),
                        workplaceTypeSpinner.toString(),
                        jobTypeEditText.toString(),
                        receivedJob.datePosted,
                        receivedJob.jobListingStatus,
                        jobLocationEditText.toString()
                    )



                    // Navigate back to the job list or perform any other necessary action
                    addJobViewModel.viewModelScope.launch(Dispatchers.IO) {
                        //Add to Database
                        addJobViewModel.updateJobs(updatedJob)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(), "Successfully updated!", Toast.LENGTH_LONG
                            ).show()
                            //Navigate back to list fragment screen
                            findNavController().navigate(R.id.action_edit_job_to_list_job)

                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG
                    ).show()

                }


            }

        }

        return view
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


}


