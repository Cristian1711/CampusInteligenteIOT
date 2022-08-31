package com.example.campusinteligenteiot.usecases.appointments

import com.example.campusinteligenteiot.api.model.appointments.AppointmentsCall
import com.example.campusinteligenteiot.repository.UserRepository

class SaveAppointmentsUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(id: String, appointments: AppointmentsCall) = repository.saveAppointments(appointments, id)
}