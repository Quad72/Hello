package com.example.ch15

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class MyJobService : JobService() {

    override fun onCreate() {
        super.onCreate()
        Log.d("kkang","MyJobService......onCreate()")
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d("kkang","MyJobService......onStartJob()")
        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d("kkang","MyJobService......onStopJob()")
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("kkang","MyJobService......onDestroy()")
    }
}