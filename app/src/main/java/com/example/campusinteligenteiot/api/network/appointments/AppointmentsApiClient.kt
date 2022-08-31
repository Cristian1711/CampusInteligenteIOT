package com.example.campusinteligenteiot.api.network.appointments

import com.example.campusinteligenteiot.api.model.appointments.AppointmentsCall
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface AppointmentsApiClient {

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/appointments/saveAppointments/{id}")
    suspend fun saveAppointments(@Body params: AppointmentsCall, @Path("id") id: String): Response<String>
}