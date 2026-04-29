package com.courseworktracker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.courseworktracker.worker.DeadlineWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class CourseworkTrackerApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleDeadlineNotifications()
    }

    private fun scheduleDeadlineNotifications() {
        val workRequest = PeriodicWorkRequestBuilder<DeadlineWorker>(
            1, TimeUnit.HOURS // Check every hour
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "deadline_notifications",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
