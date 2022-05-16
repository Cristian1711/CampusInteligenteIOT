package com.example.campusinteligenteiot.api.network

import com.example.campusinteligenteiot.api.model.UsersResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserApiClient {
    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/findUser/{id}")
    suspend fun getUserById(@Path("id") id:String): Response<UsersResponse>

    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/allUsers")
    suspend fun getAllUsers(): Response<List<UsersResponse>>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/saveUser/{id}")
    suspend fun saveUser(@Body params: UsersResponse, @Path("id") id: String): Response<String>
}