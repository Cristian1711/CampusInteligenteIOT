package com.example.campusinteligenteiot.usecases.trip

import androidx.lifecycle.LiveData
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.repository.EventRepository
import com.example.campusinteligenteiot.repository.TripRepository

class GetAllTripsUseCase {

    private val repository = TripRepository()

    suspend operator fun invoke(): LiveData<MutableList<TripResponse>>? = repository.getAllTrips()
}