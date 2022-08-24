package com.example.campusinteligenteiot.usecases.trip

import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.event.TripCall
import com.example.campusinteligenteiot.repository.EventRepository
import com.example.campusinteligenteiot.repository.TripRepository
import retrofit2.Response

class SaveTripUseCase {
    private val repository = TripRepository()

    suspend operator fun invoke(id: String, trip: TripCall): Response<String> = repository.saveTrip(trip, id)

}