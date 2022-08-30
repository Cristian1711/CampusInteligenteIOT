package com.example.campusinteligenteiot.ui.drawer.calendar.appointments

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.appointments.Appointments
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.appointments.SaveAppointmentsUseCase
import com.example.campusinteligenteiot.usecases.user.SaveUserUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase

class EditAppointmentViewModel : ViewModel() {
    val saveAppointmentsUseCase = SaveAppointmentsUseCase()
    val saveUserUseCase = SaveUserUseCase()
    val searchUserUseCase = SearchUserUseCase()

    suspend fun saveAppointments(appointments: Appointments, id: String){
        saveAppointmentsUseCase(id, appointments)
    }

    suspend fun getUser(id: String): UsersResponse{
        return searchUserUseCase(id)!!
    }

    suspend fun updateAppointments(user: UsersResponse, id: String){
        saveUserUseCase(id, user)
    }
}