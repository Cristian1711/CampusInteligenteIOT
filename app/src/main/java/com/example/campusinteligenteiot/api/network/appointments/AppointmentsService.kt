package com.example.campusinteligenteiot.api.network.appointments

import com.example.campusinteligenteiot.api.config.RetrofitBuilder
import com.example.campusinteligenteiot.api.model.appointments.Appointments
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AppointmentsService {

    private val retrofit = RetrofitBuilder.getRetrofit()

    suspend fun saveAppointments(appointments: Appointments, id: String) : Response<String> {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(AppointmentsApiClient::class.java).saveAppointments(appointments, id)
            response
        }
    }
}