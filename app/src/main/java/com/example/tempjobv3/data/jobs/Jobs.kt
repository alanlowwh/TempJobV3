package com.example.tempjobv3.data.jobs

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "JobListing")
data class Jobs(
    @PrimaryKey(autoGenerate = true)
    val jobListingId: Int = 0,

    var jobTitle: String,
    var companyName: String,
    var salary: Int

)