//package com.example.tempjobv3.ui.user
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.tempjobv3.R
//import com.example.tempjobv3.data.jobs.Jobs
//
//class CusJobListAdapter(private val jobList: List<Jobs>) : RecyclerView.Adapter<CusJobListAdapter.MyViewHolder>() {
//
//    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val jobTitleTextView: TextView = itemView.findViewById(R.id.jobTitleTextView)
//        val companyNameTextView: TextView = itemView.findViewById(R.id.companyNameTextView)
//        val salaryTextView: TextView = itemView.findViewById(R.id.salaryTextView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_job, parent, false)
//        return MyViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val currentItem = jobList[position]
//        holder.jobTitleTextView.text = currentItem.jobTitle
//        holder.companyNameTextView.text = currentItem.companyName
//        holder.salaryTextView.text = currentItem.jobListingId.toString()
//    }
//
//    override fun getItemCount(): Int {
//        return jobList.size
//    }
//}
