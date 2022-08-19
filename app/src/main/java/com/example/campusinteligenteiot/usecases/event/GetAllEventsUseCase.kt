package com.example.campusinteligenteiot.usecases.event

import androidx.lifecycle.LiveData
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.repository.EventRepository

class GetAllEventsUseCase {

    private val repository = EventRepository()

    suspend operator fun invoke(): LiveData<MutableList<EventResponse>>? = repository.getAllEvents()
}