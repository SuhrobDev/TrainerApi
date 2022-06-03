package com.example.newtrainerapp.app

import android.app.Application
import com.example.newtrainerapp.retrofit.ApiClient
import com.example.newtrainerapp.room.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(applicationContext)
        ApiClient.instance(applicationContext)
    }
}