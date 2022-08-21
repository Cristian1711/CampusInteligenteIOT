package com.example.campusinteligenteiot.api.network.event

import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import retrofit2.Response
import retrofit2.http.*

interface EventApiClient {
    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/events/getEvent/{id}")
    suspend fun getEventById(@Path("id") id:String): Response<EventResponse>

    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/events/allEvents")
    suspend fun getAllEvents(): List<EventResponse>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/events/saveEvent/{id}")
    suspend fun saveEvent(@Body params: EventCall, @Path("id") id: String): Response<String>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/events/deleteEvent/{id}")
    suspend fun deleteEvent(@Path("id") id: String): Response<EventResponse>
}