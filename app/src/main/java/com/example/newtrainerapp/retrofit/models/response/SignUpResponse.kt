package com.example.newtrainerapp.retrofit.models.response


import com.google.gson.annotations.SerializedName

data class SignUpResponse(
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