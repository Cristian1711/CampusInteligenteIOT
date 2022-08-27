package com.example.campusinteligenteiot.usecases.trip

import androidx.lifecycle.LiveData
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.repository.TripRepository

class GetSingleTripLiveUseCase {

    private val repository = TripRepository()

    suspend operator fun invoke(id:String): LiveData<TripResponse> = repository.getTripByIdLive(id)
}