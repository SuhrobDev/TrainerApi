package com.example.newtrainerapp.app

import android.app.Application
import com.example.newtrainerapp.room.AppDatabase
import com.example.newtrainerapp.retrofit.ApiClient

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(applicationContext)
        ApiClient.instance()
    }
}