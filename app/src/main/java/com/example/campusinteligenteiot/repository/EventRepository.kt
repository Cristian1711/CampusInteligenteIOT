package com.example.campusinteligenteiot.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.network.event.EventService
import retrofit2.Response

class EventRepository {

    private val api = EventService()

    suspend fun getEventById(id: String): EventResponse {
        return api.getEventById(id)
    }

    suspend fun getEventByIdLive(id: String): MutableLiveData<EventResponse> {
        return api.getEventByIdLive(id)
    }

    suspend fun getAllEvents(): LiveData<MutableList<EventResponse>>? {
        return api.getAllEvents()
    }

    suspend fun saveEvent(event: EventCall, id: String) : Response<String> {
        return api.saveEvent(event, id)
    }

    suspend fun deleteEvent(id: String) : EventResponse {
        return api.deleteEvent(id)
    }
}