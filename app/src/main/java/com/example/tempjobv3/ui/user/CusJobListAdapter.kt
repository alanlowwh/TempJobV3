// CusJobListAdapter.kt
package com.example.tempjobv3.ui.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.Jobs

class CusJobListAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<CusJobListAdapter.MyViewHolder>() {

    private var jobList = emptyList<Jobs>()

    interface OnItemClickListener {
        fun onItemClick(job: Jobs)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitleTextView: TextView = itemView.findViewById(R.id.jobTitle_textView)
        val companyNameTextView: TextView = itemView.findViewById(R.id.companyName_textView)
        val salaryTextView: TextView = itemView.findViewById(R.id.salary_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_job, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.jobTitleTextView.text = currentItem.jobTitle
        holder.companyNameTextView.text = currentItem.companyName
        holder.salaryTextView.text = currentItem.salary.toString()

        holder.itemView.setOnClickListener {
            listener.onItemClick(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    fun setData(jobs: List<Jobs>) {
        this.jobList = jobs
        notifyDataSetChanged()
    }
}
