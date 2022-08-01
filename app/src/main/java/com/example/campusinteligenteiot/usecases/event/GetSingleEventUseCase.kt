package com.example.campusinteligenteiot.usecases.event

import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.repository.EventRepository

class GetSingleEventUseCase {

    private val repository = EventRepository()

    suspend operator fun invoke(id:String): EventResponse? = repository.getEventById(id)
}