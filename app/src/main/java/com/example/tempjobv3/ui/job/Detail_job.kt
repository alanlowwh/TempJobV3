package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.Jobs


class detail_job : Fragment() {

    private lateinit var receivedJob: Jobs


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Use Safe Args to retrieve the passed argument
        arguments?.let {
            val args = detail_jobArgs.fromBundle(it)
            receivedJob = args.selectedJobToDetail
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_job, container, false)

        if (receivedJob != null) {
            val jobTitle = view.findViewById<TextView>(R.id.detail_jobTitleTextView)
            val companyName = view.findViewById<TextView>(R.id.detail_companyNameTextView)
            val salary = view.findViewById<TextView>(R.id.detail_salary)
            val workplaceTypeEditText = view.findViewById<TextView>(R.id.detail_workplaceType)
            val jobLocationEditText = view.findViewById<TextView>(R.id.detail_jobLocation)
            val jobTypeEditText = view.findViewById<TextView>(R.id.detail_jobType)
            val jobDescriptionEditText = view.findViewById<TextView>(R.id.detail_jobDescription)
            val datePosted = view.findViewById<TextView>(R.id.detail_datePosted)

            jobTitle.text = receivedJob.jobTitle
            companyName.text = receivedJob.companyName
            salary.text = receivedJob.salary.toString()
            workplaceTypeEditText.text = receivedJob.workplaceType
            jobLocationEditText.text = receivedJob.jobLocation
            jobTypeEditText.text = receivedJob.jobType
            jobDescriptionEditText.text = receivedJob.jobDescription
            datePosted.text = receivedJob.datePosted

//            jobTitle.setText(receivedJob.jobTitle)
//            companyName.setText(receivedJob.companyName)
//            salary.setText(receivedJob.salary)


            //            workplaceTypeEditText.setText(receivedJob.workplaceType)
            //            jobLocationEditText.setText(receivedJob.jobLocation)
            //            jobTypeEditText.setText(receivedJob.jobType)
            //            jobDescriptionEditText.setText(receivedJob.jobDescription)

            //Pass object Job and navigate to job application
            val viewJobAppButton = view.findViewById<Button>(R.id.viewJobApplicationButton)
            viewJobAppButton.setOnClickListener{

            }


        }

        return view
    }


}