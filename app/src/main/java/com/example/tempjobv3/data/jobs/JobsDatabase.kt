package com.example.tempjobv3.data.jobs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Jobs::class], version = 3, exportSchema = false)
abstract class JobsDatabase : RoomDatabase() {
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
                    "JobListing"
                )
                    .addMigrations(MIGRATION_2_3) // Define your migrations here
                    .fallbackToDestructiveMigration() // Allow for destructive migrations
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        // Define your migrations here
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Define your migration SQL statements here, if needed
                // For example, if you need to rename a column, you can do it here
            }
        }
    }
}
