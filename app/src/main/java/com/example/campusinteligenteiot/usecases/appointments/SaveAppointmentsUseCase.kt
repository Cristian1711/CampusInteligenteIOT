package com.example.campusinteligenteiot.usecases.appointments

import com.example.campusinteligenteiot.api.model.appointments.Appointments
import com.example.campusinteligenteiot.repository.AppointmentsRepository

class SaveAppointmentsUseCase {

    private val repository = AppointmentsRepository()

    suspend operator fun invoke(id: String, appointments: Appointments) = repository.saveAppointments(appointments, id)
}