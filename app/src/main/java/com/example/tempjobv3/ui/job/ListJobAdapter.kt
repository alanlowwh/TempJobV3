package com.example.tempjobv3.ui.job

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.navigation.fragment.findNavController




import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tempjobv3.R
import com.example.tempjobv3.data.jobs.Jobs

class ListJobAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ListJobAdapter.MyViewHolder>() {

    private var jobList = emptyList<Jobs>()


    //Pass data safeargs
    interface OnItemClickListener {
        fun onItemClick(job: Jobs)
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitleTextView: TextView = itemView.findViewById(R.id.jobTitle_textView)
        val companyNameTextView: TextView = itemView.findViewById(R.id.companyName_textView)
        val salaryTextView: TextView = itemView.findViewById(R.id.salary_textView)
        val popupMenuButton: View = itemView.findViewById(R.id.popUpMenuButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_job_row, parent, false)
        )
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = jobList[position]
        holder.itemView.findViewById<TextView>(R.id.jobTitle_textView).text = currentItem.jobTitle
        holder.itemView.findViewById<TextView>(R.id.companyName_textView).text =
            currentItem.companyName
        holder.itemView.findViewById<TextView>(R.id.salary_textView).text =
            currentItem.salary.toString()


        //pass data safeargs
        holder.itemView.setOnClickListener {
            listener.onItemClick(currentItem)
        }

        holder.popupMenuButton.setOnClickListener() {
            val popupMenu = PopupMenu(holder.itemView.context, holder.popupMenuButton)
            popupMenu.menuInflater.inflate(R.menu.managejob_popup_menu, popupMenu.menu)

            // Set an item click listener for the menu items
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_edit -> {
                        // Handle the edit action
                        val action = ListJobFragmentDirections.actionListJobToEditJob(currentItem)
                        holder.itemView.findNavController().navigate(action)


                        // You can perform edit operations here
                        true
                    }

                    R.id.menu_delete -> {
                        // Handle the delete action
                        // You can perform delete operations here
                        true
                    }

                    else -> false
                }
            }

            // Show the popup menu
            popupMenu.show()
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