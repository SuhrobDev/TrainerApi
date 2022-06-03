package com.example.newtrainerapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trainer")
data class Trainer(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "type")
    var type: Int,
    @ColumnInfo(name = "trainer_id")
    var trainerId: Int,
    @ColumnInfo(name = "trainer_name")
    var name: String,
    @ColumnInfo(name = "trainer_surname")
    var surname: String,
    @ColumnInfo(name = "trainer_salary")
    var salary: Double
)