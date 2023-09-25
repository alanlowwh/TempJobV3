package com.example.tempjobv3.ui.job

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.AddJobViewModel
import com.example.tempjobv3.data.jobs.Jobs
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass.
 * Use the [list_job.newInstance] factory method to
 * create an instance of this fragment.
 */
class list_job : Fragment() {


    private lateinit var addJobViewModel: AddJobViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_job, container, false)

        //RecyclerView
        val adapter = ListJobAdapter(object : ListJobAdapter.OnItemClickListener {
            override fun onItemClick(job: Jobs) {
                // Handle item click here, e.g., navigate to the edit_job fragment with the selected job
                val action = list_jobDirections.actionListJobToEditJob(job)
                findNavController().navigate(action)
            }
        })
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerviewJob)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //JobViewModel
        addJobViewModel = ViewModelProvider(this).get(AddJobViewModel::class.java)
        addJobViewModel.readAllJob.observe(viewLifecycleOwner, Observer{ jobs ->
            adapter.setData(jobs)
        })




        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener() {
            findNavController().navigate(R.id.action_list_job_to_add_job)
        }



        return view
    }


}