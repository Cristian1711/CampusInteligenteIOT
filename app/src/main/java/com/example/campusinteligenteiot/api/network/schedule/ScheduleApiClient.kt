package com.example.campusinteligenteiot.api.network.schedule

import com.example.campusinteligenteiot.api.model.schedule.ScheduleResponse
import retrofit2.Response
import retrofit2.http.*

interface ScheduleApiClient {
    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/trainSchedule/getSchedule/{id}")
    suspend fun getScheduleById(@Path("id") id:String): Response<ScheduleResponse>

    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/trainSchedule/fullSchedule")
    suspend fun getFullSchedule(): Response<List<ScheduleResponse>>

}