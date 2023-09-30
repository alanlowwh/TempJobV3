package com.example.tempjobv3.data.jobs

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class JobsRepository(private val jobsDao: JobsDao) {

    val readAllData: LiveData<List<Jobs>> = jobsDao.readAllData()


     fun addJobs(jobs: Jobs) {
        try {
            jobsDao.addJobs(jobs)
        } catch (e: Exception) {
            Log.e("YourTag", "An error occurred in addJobs: ${e.message}", e)

        }
    }

    fun updateJobs(jobs: Jobs){
        jobsDao.updateJobs(jobs)
    }

    fun deleteJobs(jobs: Jobs){
        jobsDao.deleteJob(jobs)
    }

    fun getLastInsertedId(): Long{
        return jobsDao.getLastInsertedId()
    }

//    fun getLastInsertedId(): LiveData<Long>{
//        return jobsDao.getLastInsertedId()
//    }


//    @WorkerThread
//    suspend fun insert(job: Jobs): Boolean {
//
//        try {
//            jobsDao.insert(job)
//            return true
//
//        } catch (e: Exception) {
//            return false
//        }
//    }
//
//    @WorkerThread
//    suspend fun delete(job: Jobs) {
//        jobsDao.delete(job)
//    }
}