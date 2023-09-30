package com.example.tempjobv3.data.user

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class JobApplication(
    val jobApplicationId: String,
    val jobListingId: String,
    val customerId: String,
    var status: String,
    var jobApplicationDate: String,
    var jobTitle: String? = null, // Add mutable properties for job details
    var companyName: String? = null,
    var companyAddress: String? = null
) {
    // Add a no-argument constructor
    constructor() : this("", "", "", "", "")
}
