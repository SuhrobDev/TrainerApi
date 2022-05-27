package com.example.newtrainerapp.retrofit.models.response

data class TrainerResponse(
    var id: Int = 0,
    var trainerName: String,
    var trainerSalary: Double,
    var trainerSurname: String
)