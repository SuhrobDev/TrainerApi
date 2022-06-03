package com.example.newtrainerapp.retrofit.models.response

import com.google.gson.annotations.SerializedName

data class LogInResponse(
    @SerializedName("accessToken")
    var accessToken: String,
    @SerializedName("tokenType")
    var tokenType: String
)