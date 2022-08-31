package com.example.campusinteligenteiot.repository

import com.example.campusinteligenteiot.api.model.appointments.AppointmentsCall
import com.example.campusinteligenteiot.api.network.appointments.AppointmentsService
import retrofit2.Response

class AppointmentsRepository {

    private val api = AppointmentsService()

    suspend fun saveAppointments(appointments: AppointmentsCall, id: String) : Response<String> {
        return api.saveAppointments(appointments, id)
    }
}