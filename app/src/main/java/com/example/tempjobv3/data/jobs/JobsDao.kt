package com.example.tempjobv3.data.jobs
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface JobsDao {

//    @Query("SELECT * FROM JobListing")
//    suspend fun getAllJobs(): List<Jobs>

//    @Insert
//    suspend fun get(jobPost: Jobs)
//
//    @Delete
//    suspend fun delete(jobPost: Jobs)

    @Insert
     fun addJobs(jobs: Jobs)

     @Update
     fun updateJobs(jobs: Jobs)

    @Query("SELECT * FROM JobListing WHERE jobListingStatus = 'Available'")
     fun readAllData(): LiveData<List<Jobs>>

    @Delete
    fun deleteJob(job: Jobs)





}