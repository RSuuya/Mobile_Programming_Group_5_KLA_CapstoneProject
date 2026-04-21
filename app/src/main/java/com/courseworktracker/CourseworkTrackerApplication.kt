package com.courseworktracker

import android.app.Application
import com.courseworktracker.repository.AppDatabase
import com.courseworktracker.repository.AssignmentRepository

class CourseworkTrackerApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { AssignmentRepository(database.assignmentDao()) }
}
