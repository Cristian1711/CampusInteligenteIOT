package com.example.campusinteligenteiot.usecases.event

import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.repository.EventRepository

class SaveEventUseCase {
    private val repository = EventRepository()

    suspend operator fun invoke(id: String, event: EventCall) = repository.saveEvent(event, id)

}