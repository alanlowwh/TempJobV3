package com.example.tempjobv3.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Applicant")
data class User(
    var name: String = "", // Provide default values for properties
    var email: String = "",
    var phone: String = "",
    var gender: String = "",
    var password: String = "",
    var jobTitle: String = "",
    var role: String = "customer"
) {
    // No-argument constructor required by Firebase
    constructor() : this("", "", "", "", "", "", "")
}