package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.Jobs
import com.google.android.material.bottomnavigation.BottomNavigationView


class detail_job : Fragment() {

    private lateinit var receivedJob: Jobs

    lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Use Safe Args to retrieve the passed argument
        arguments?.let {
            val args = detail_jobArgs.fromBundle(it)
            receivedJob = args.selectedJobToDetail
        }


//        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//        val bottomNavView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
//        bottomNavView.setOnItemReselectedListener { item ->
//            when (item.itemId){
//                R.id.job_btm_nav ->{
////                    val action = detail_jobDirections.actionDetailJobToListJob()
////                    findNavController().navigate(action)
//                    val destinationFragment = list_job()
//                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
//                    transaction.replace(R.id.fragmentContainerView, destinationFragment)
//                    transaction.addToBackStack(null)
//                    transaction.commit()
//
//
//
//
//                }
//                R.id.profile_btm_nav -> {
//
//                }
//            }
//        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_job, container, false)


        //Bottom nav implementation
        val bottomNavView = view.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavView.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.job_btm_nav -> {
                    val action = detail_jobDirections.actionDetailJobToListJob()
                    findNavController().navigate(action)
                }
                // Handle other navigation cases here
            }
        }



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


            //Pass object Job and navigate to job application
            val viewJobAppButton = view.findViewById<Button>(R.id.viewJobApplicationButton)
            viewJobAppButton.setOnClickListener {

            }


        }

        return view
    }


}