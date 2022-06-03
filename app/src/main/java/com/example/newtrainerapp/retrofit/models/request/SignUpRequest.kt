package com.example.newtrainerapp.retrofit.models.request


import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("role")
    var role: List<String>,
    @SerializedName("password")
    var password: String
)