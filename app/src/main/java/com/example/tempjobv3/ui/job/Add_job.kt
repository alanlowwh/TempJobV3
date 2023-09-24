package com.example.tempjobv3.ui.job

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tempjobv3.data.jobs.Jobs
import com.example.tempjobv3.data.jobs.JobsRepository
import com.example.tempjobv3.databinding.FragmentAddJobBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.withContext

import com.example.tempjobv3.data.jobs.AddJobViewModel
import com.example.tempjobv3.data.jobs.JobsDatabase

class add_job : Fragment() {


    private lateinit var mAddJobViewModel: AddJobViewModel
    private lateinit var binding: FragmentAddJobBinding // Declare a binding variable
    private lateinit var jobRepo: JobsRepository


    companion object {
        fun newInstance() = add_job()
    }

    private lateinit var viewModel: AddJobViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using data binding
        binding = FragmentAddJobBinding.inflate(inflater, container, false)
        val view = binding.root // Get the root view from the binding

        // Initialize the ViewModel
        mAddJobViewModel = ViewModelProvider(this).get(AddJobViewModel::class.java)


        val jobsDao = JobsDatabase.getDatabase(requireContext()).jobsDao()

        // Initialize the repository (make sure to pass the necessary dependencies)
        jobRepo = JobsRepository(jobsDao)


        // Set the ViewModel in the binding
        binding.viewModel = mAddJobViewModel

        // Set the lifecycle owner for LiveData in the binding
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


        val job = Jobs(jobTitle = jobTitle, companyName = companyName, salary = salaryText.toInt())


        mAddJobViewModel.viewModelScope.launch(Dispatchers.IO) {
            jobRepo.addJobs(job)
        }

//        jobRepo.addJobs(job)

    }

}