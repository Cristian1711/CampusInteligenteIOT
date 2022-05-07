package com.example.campusinteligenteiot.api.network

import com.example.campusinteligenteiot.api.model.UsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiClient {
    @GET("http://149.91.99.198:8080/user/api/v1/find/{id}")
    suspend fun getUserById(@Path("id") id:String): Response<UsersResponse>

    @GET("http://149.91.99.198:8080/user/api/v1/all")
    suspend fun getAllUsers(): Response<List<UsersResponse>>
}