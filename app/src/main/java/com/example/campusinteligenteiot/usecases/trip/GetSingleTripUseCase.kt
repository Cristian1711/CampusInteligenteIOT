package com.example.campusinteligenteiot.usecases.trip

import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.event.TripResponse
import com.example.campusinteligenteiot.repository.EventRepository
import com.example.campusinteligenteiot.repository.TripRepository

class GetSingleTripUseCase {

    private val repository = TripRepository()

    suspend operator fun invoke(id:String): TripResponse? = repository.getTripById(id)
}