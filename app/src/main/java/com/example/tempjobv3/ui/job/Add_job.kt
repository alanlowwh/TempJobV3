package com.example.tempjobv3.ui.job


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tempjobv3.data.jobs.AddJobViewModel
import com.example.tempjobv3.data.jobs.Jobs
import com.example.tempjobv3.data.jobs.JobsRepository
import com.example.tempjobv3.databinding.FragmentAddJobBinding
import com.example.tempjobv3.data.jobs.JobsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.tempjobv3.R
import kotlinx.coroutines.withContext

class add_job : Fragment() {

    private lateinit var mAddJobViewModel: AddJobViewModel
    private lateinit var binding: FragmentAddJobBinding
    private lateinit var jobRepo: JobsRepository

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
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val jobTitle = binding.jobTitleEditText.text.toString()
        val companyName = binding.companyNameEditText.text.toString()
        val salaryText = binding.salaryEditText.text.toString()

        if (inputCheck(jobTitle, companyName, salaryText)) {
            val salary = salaryText.toInt()
            val job = Jobs(jobTitle = jobTitle, companyName = companyName, salary = salary)

            mAddJobViewModel.viewModelScope.launch(Dispatchers.IO) {
                //Add to Database
                mAddJobViewModel.addJobs(job)
                withContext(Dispatchers.Main) {
                    // Show success message and navigate to another fragment
                    Toast.makeText(requireContext(), "Successfully posted!", Toast.LENGTH_LONG)
                        .show()
                    //Navigate back to list fragment screen
                    findNavController().navigate(R.id.action_add_job_to_list_job)

                }
            }
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG)
                .show()

        }
    }


    private fun inputCheck(jobTitle: String, companyName: String, salary: String): Boolean {
        return !(TextUtils.isEmpty(jobTitle) || TextUtils.isEmpty(companyName) || TextUtils.isEmpty(
            salary
        ))
    }


}
