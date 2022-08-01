package com.example.campusinteligenteiot.usecases.event

import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.repository.EventRepository

class GetAllEventsUseCase {

    private val repository = EventRepository()

    suspend operator fun invoke(): List<EventResponse>? = repository.getAllEvents()
}