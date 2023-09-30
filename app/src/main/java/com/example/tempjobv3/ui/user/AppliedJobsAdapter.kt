package com.example.tempjobv3.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tempjobv3.R
import com.example.tempjobv3.data.user.JobApplication

class AppliedJobsAdapter(private val jobApplications: List<JobApplication>) :
    RecyclerView.Adapter<AppliedJobsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitleTextView: TextView = itemView.findViewById(R.id.jobTitleTextView)
        val companyNameTextView: TextView = itemView.findViewById(R.id.companyNameTextView)
        val companyAddressTextView: TextView = itemView.findViewById(R.id.companyAddressTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_applied_job, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentJobApplication = jobApplications[position]

        // Set job details from your JobApplication object
        holder.jobTitleTextView.text = currentJobApplication.jobTitle
        holder.companyNameTextView.text = currentJobApplication.companyName
        holder.companyAddressTextView.text = currentJobApplication.companyAddress
        holder.statusTextView.text = currentJobApplication.status
    }

    override fun getItemCount(): Int {
        return jobApplications.size
    }
}
