package com.example.campusinteligenteiot.api.network.trip

import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.event.TripCall
import com.example.campusinteligenteiot.api.model.event.TripResponse
import retrofit2.Response
import retrofit2.http.*

interface TripApiClient {
    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/trips/getTrip/{id}")
    suspend fun getTripById(@Path("id") id:String): Response<TripResponse>

    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/trips/allTrips")
    suspend fun getAllTrips(): List<TripResponse>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/trips/saveTrip/{id}")
    suspend fun saveTrip(@Body params: TripCall, @Path("id") id: String): Response<String>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/trips/deleteTrip/{id}")
    suspend fun deleteTrip(@Path("id") id: String): Response<TripResponse>
}