package com.example.newtrainerapp.retrofit

import com.example.newtrainerapp.retrofit.models.request.LogInRequest
import com.example.newtrainerapp.retrofit.models.request.SignUpRequest
import com.example.newtrainerapp.retrofit.models.request.TrainerRequest
import com.example.newtrainerapp.retrofit.models.response.LogInResponse
import com.example.newtrainerapp.retrofit.models.response.SignUpResponse
import com.example.newtrainerapp.retrofit.models.response.TrainerResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("trainer_with_role")
    fun getTrainersList(): Call<List<TrainerResponse>>

    @POST("trainer_with_role")
    fun addTrainer(@Body trainer: TrainerRequest): Call<TrainerResponse>

    @DELETE("trainer_with_role/{id}")
    fun deleteTrainer(@Path("id") id: Int): Call<TrainerResponse>

    @PUT("trainer_with_role/{id}")
    fun editTrainer(@Body trainer: TrainerRequest, @Path("id") id: Int): Call<TrainerResponse>

    @POST("auth/signin")
    fun logIn(@Body login: LogInRequest): Call<LogInResponse>

    @POST("auth/signup")
    fun signUp(@Body logup: SignUpRequest): Call<SignUpResponse>
}