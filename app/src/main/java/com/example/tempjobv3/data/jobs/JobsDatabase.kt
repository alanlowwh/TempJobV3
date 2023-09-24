package com.example.tempjobv3.data.jobs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Jobs::class], version = 8, exportSchema = false)
abstract class JobsDatabase : RoomDatabase(){
    abstract fun jobsDao(): JobsDao


    companion object {
        @Volatile
        private var INSTANCE: JobsDatabase? = null

        fun getDatabase(context: Context): JobsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JobsDatabase::class.java,
                    "jobs_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}