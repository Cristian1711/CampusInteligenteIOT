package com.example.campusinteligenteiot.api.network

import com.example.campusinteligenteiot.api.model.UsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiClient {
    @GET("/find/{id}")
    suspend fun getUserById(@Path("id") id:String): Response<UsersResponse>

    @GET("/all")
    suspend fun getAllUsers(): Response<List<UsersResponse>>
}