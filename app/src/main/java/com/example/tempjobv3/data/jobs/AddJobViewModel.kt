package com.example.tempjobv3.data.jobs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddJobViewModel(application: Application) : AndroidViewModel(application) {

    val readAllJob: LiveData<List<Jobs>>
    private val repository: JobsRepository

    init {
        val jobsDao = JobsDatabase.getDatabase(application).jobsDao()
        repository = JobsRepository(jobsDao)
        readAllJob = repository.readAllData

    }

     fun addJobs(jobs: Jobs){
        viewModelScope.launch(Dispatchers.IO){
            repository.addJobs(jobs)
        }
    }

    fun updateJobs(jobs: Jobs){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateJobs(jobs)
        }
    }


}