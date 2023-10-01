package com.example.tempjobv3.ui.job

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tempjobv3.R
import com.example.tempjobv3.data.user.JobApplication

class ApplicationJobAdapter(
    private val appliedJobsList: List<JobApplication>
) : RecyclerView.Adapter<ApplicationJobAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitleTextView: TextView = itemView.findViewById(R.id.jobTitleTextView)
        val companyNameTextView: TextView = itemView.findViewById(R.id.companyNameTextView)
        val jobLocationTextView: TextView = itemView.findViewById(R.id.jobLocationTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.application_job, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jobApplication = appliedJobsList[position]
        holder.jobTitleTextView.text = jobApplication.jobTitle
        holder.companyNameTextView.text = jobApplication.companyName
        holder.jobLocationTextView.text = jobApplication.jobLocation
        holder.statusTextView.text = jobApplication.status
    }

    override fun getItemCount(): Int {
        return appliedJobsList.size
    }
}
