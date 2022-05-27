package com.example.newtrainerapp.retrofit

import retrofit2.Call
import retrofit2.http.*
import com.example.newtrainerapp.retrofit.models.request.TrainerRequest
import com.example.newtrainerapp.retrofit.models.response.TrainerResponse

interface ApiInterface {

    @GET("trainer")
    fun getTrainersList(): Call<List<TrainerResponse>>

    @POST("trainer")
    fun addTrainer(@Body trainer: TrainerRequest): Call<TrainerResponse>

    @DELETE("trainer/{id}")
    fun deleteTrainer(@Path("id") id: Int): Call<TrainerResponse>

    @PUT("trainer/{id}")
    fun editTrainer(@Body trainer: TrainerRequest, @Path("id") id: Int): Call<TrainerResponse>
}