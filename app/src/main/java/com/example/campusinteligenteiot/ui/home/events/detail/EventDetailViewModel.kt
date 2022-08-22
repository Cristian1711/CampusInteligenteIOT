package com.example.campusinteligenteiot.ui.home.events.detail

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.usecases.event.GetSingleEventUseCase
import com.example.campusinteligenteiot.usecases.event.SaveEventUseCase
import retrofit2.Response

class EventDetailViewModel : ViewModel() {
    val getSingleEventUseCase = GetSingleEventUseCase()
    val saveEventUseCase = SaveEventUseCase()

    suspend fun getSingleEvent(id: String): EventResponse{
        return getSingleEventUseCase(id)!!
    }

    suspend fun updateEvent(id: String, event: EventCall): Response<String> {
        return saveEventUseCase(id, event)
    }
}