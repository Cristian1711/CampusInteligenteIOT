package com.example.campusinteligenteiot.usecases.event

import androidx.lifecycle.LiveData
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.repository.EventRepository

class GetSingleEventLiveUseCase {

    private val repository = EventRepository()

    suspend operator fun invoke(id:String): LiveData<EventResponse> = repository.getEventByIdLive(id)
}