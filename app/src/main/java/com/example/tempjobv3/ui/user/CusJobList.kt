package com.example.tempjobv3.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.AddJobViewModel
import com.example.tempjobv3.data.jobs.Jobs

class CusJobList : Fragment() {

    private lateinit var addJobViewModel: AddJobViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cus_job_list, container, false)

        val adapter = CusJobListAdapter(object : CusJobListAdapter.OnItemClickListener {
            override fun onItemClick(job: Jobs) {
                // Navigate to the job details page using SafeArgs
                val action = CusJobListDirections.actionCusJobListToDetailJob(job)
                findNavController().navigate(action)
            }
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerviewJob)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        addJobViewModel = ViewModelProvider(this).get(AddJobViewModel::class.java)
        addJobViewModel.readAllJob.observe(viewLifecycleOwner, Observer { jobs ->
            adapter.setData(jobs)
        })

        return view
    }
}
