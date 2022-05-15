package com.example.campusinteligenteiot.api.network

import com.example.campusinteligenteiot.api.model.UsersResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserApiClient {
    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/user/api/v1/find/{id}")
    suspend fun getUserById(@Path("id") id:String): Response<UsersResponse>

    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/user/api/v1/all")
    suspend fun getAllUsers(): Response<List<UsersResponse>>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/user/api/v1/save/{id}")
    suspend fun saveUser(@Body params: UsersResponse, @Path("id") id: String): Response<String>
}