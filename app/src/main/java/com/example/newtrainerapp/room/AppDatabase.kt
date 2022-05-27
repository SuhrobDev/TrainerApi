package com.example.newtrainerapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newtrainerapp.entity.Trainer

@Database(
    entities = [
        Trainer::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun inputData(): InputDao

    companion object {
        var database: AppDatabase? = null
        fun init(context: Context) {
            database = Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "my_room_db"
            )
                .allowMainThreadQueries()
                .build()
        }
    }
}