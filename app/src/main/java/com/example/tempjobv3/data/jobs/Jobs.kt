package com.example.tempjobv3.data.jobs

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Entity(tableName = "JobListing")
@Parcelize

data class Jobs(
    @PrimaryKey(autoGenerate = true)
    @get:PropertyName("jobListingId")
    @set:PropertyName("jobListingId")
    var jobListingId: Int = 0,
    var jobReferences: String,
    var jobTitle: String,
    var companyName: String,
    var salary: Int,
    var jobDescription: String,
    var workplaceType: String,
    var jobType: String,
    var datePosted: String,
    var jobListingStatus: String,
    var jobLocation: String


): Parcelable
