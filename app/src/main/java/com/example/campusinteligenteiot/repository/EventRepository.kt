package com.example.campusinteligenteiot.repository

import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.network.event.EventService
import retrofit2.Response

class EventRepository {

    private val api = EventService()

    suspend fun getEventById(id: String): EventResponse {
        return api.getEventById(id)
    }

    suspend fun getAllEvents(): List<EventResponse> {
        return api.getAllEvents()
    }

    suspend fun saveEvent(event: EventResponse, id: String) : Response<String> {
        return api.saveEvent(event, id)
    }

    suspend fun deleteEvent(id: String) : EventResponse {
        return api.deleteEvent(id)
    }
}