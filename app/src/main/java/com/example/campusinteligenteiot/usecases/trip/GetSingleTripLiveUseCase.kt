package com.example.campusinteligenteiot.usecases.trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.repository.TripRepository

class GetSingleTripLiveUseCase {

    private val repository = TripRepository()

    suspend operator fun invoke(id:String): MutableLiveData<TripResponse> = repository.getTripByIdLive(id)
}