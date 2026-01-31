package com.wildticker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        // Schedule periodic fetch worker for headlines
        scheduleFetchWorker()
    }

    private fun scheduleFetchWorker() {
        val work = androidx.work.PeriodicWorkRequestBuilder<com.wildticker.worker.FetchWorker>(
            java.time.Duration.ofMinutes(15)
        ).build()

        androidx.work.WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "wildticker_fetch",
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                work
            )
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
