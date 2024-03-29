package com.example.campusinteligenteiot.api.network.user

import com.example.campusinteligenteiot.api.model.appointments.AppointmentsCall
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApiClient {
    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/users/findUser/{id}")
    suspend fun getUserById(@Path("id") id:String): Response<UsersResponse>

    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/users/allUsers")
    suspend fun getAllUsers(): Response<List<UsersResponse>>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/users/saveUser/{id}")
    suspend fun saveUser(@Body params: UsersResponse, @Path("id") id: String): Response<String>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/users/saveProductLikes/{id}")
    suspend fun saveProductLikes(@Body params: ArrayList<String>, @Path("id") id: String): Response<String>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/users/saveFriends/{id}")
    suspend fun saveFriends(@Body params: ArrayList<String>, @Path("id") id: String): Response<String>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/users/saveAppointments/{id}")
    suspend fun saveAppointments(@Body params: AppointmentsCall, @Path("id") id: String): Response<String>
}