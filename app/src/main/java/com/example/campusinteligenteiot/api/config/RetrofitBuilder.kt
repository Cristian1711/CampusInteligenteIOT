package com.example.campusinteligenteiot.api.config

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://149.91.99.198:8080/user/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}