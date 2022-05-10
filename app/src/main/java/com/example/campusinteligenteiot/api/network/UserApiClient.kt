package com.example.campusinteligenteiot.api.network

import com.example.campusinteligenteiot.api.model.UsersResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiClient {
    @GET("http://149.91.99.198:8080/user/api/v1/find/{id}")
    suspend fun getUserById(@Path("id") id:String): Response<UsersResponse>

    @GET("http://149.91.99.198:8080/user/api/v1/all")
    suspend fun getAllUsers(): Response<List<UsersResponse>>

    @POST("http://149.91.99.198:8080/user/api/v1/save/{id}")
    suspend fun saveUser(@Path("id") id: String, @Body params: UsersResponse): Call<UsersResponse>
}