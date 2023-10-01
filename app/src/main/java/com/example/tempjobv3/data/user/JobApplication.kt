package com.example.tempjobv3.data.user

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class JobApplication(
    val jobApplicationId: String = "",
    val jobReferences: String = "",
    val customerId: String = "",
    var status: String = "",
    var jobApplicationDate: String = "",
    var jobTitle: String? = null,
    var companyName: String? = null,
    var jobLocation: String? = null
)
 {
    // Add a no-argument constructor
    constructor() : this("", "", "", "", "")
}
