package com.example.newtrainerapp.retrofit

import android.content.Context
import com.example.newtrainerapp.utils.SharedPref
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://microstar.herokuapp.com/api/"
    var retrofit: Retrofit? = null

    fun instance(context: Context) {
        val shared = SharedPref(context)
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addNetworkInterceptor(httpLoggingInterceptor).addInterceptor { chain ->
                        val request = chain.request()
                        val newRequest = if (shared.getToken().isNullOrEmpty())
                            request.newBuilder()
                        else request.newBuilder()
                            .header("Authorization", "Bearer ${shared.getToken()}")
                        chain.proceed(newRequest.build())//.also {
//                        if (it.code == 401) {
////                            Handler(Looper.getMainLooper()).post { shared.setAccessToken("empty") }
//                        }
                        //}
                    }
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
