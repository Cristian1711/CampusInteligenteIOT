package com.example.campusinteligenteiot.ui.home.events.manageIOT

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.usecases.event.GetSingleEventUseCase
import com.example.campusinteligenteiot.usecases.event.SaveEventUseCase

class HomeIOTViewModel : ViewModel() {
    val saveEventUseCase = SaveEventUseCase()
    val getEventUseCase = GetSingleEventUseCase()

    suspend fun getEvent(id: String) : EventResponse {
        return getEventUseCase(id)!!
    }

    suspend fun saveEvent(id: String, event: EventCall){
        saveEventUseCase(id, event)
    }
}