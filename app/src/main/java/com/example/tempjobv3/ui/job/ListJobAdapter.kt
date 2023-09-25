package com.example.tempjobv3.ui.job

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.Jobs

class ListJobAdapter : RecyclerView.Adapter<ListJobAdapter.MyViewHolder>() {

    private var jobList = emptyList<Jobs>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_job_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.itemView.findViewById<TextView>(R.id.jobTitle_textView).text = currentItem.jobTitle
        holder.itemView.findViewById<TextView>(R.id.companyName_textView).text = currentItem.companyName
        holder.itemView.findViewById<TextView>(R.id.salary_textView).text = currentItem.salary.toString()

    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    fun setData(jobs: List<Jobs>){
        this.jobList = jobs
        notifyDataSetChanged()
    }

}