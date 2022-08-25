package com.example.campusinteligenteiot.ui.home.car.driver.add

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.trip.TripCall
import com.example.campusinteligenteiot.usecases.trip.SaveTripUseCase
import retrofit2.Response

class AddTripViewModel : ViewModel() {
    val saveTripUseCase = SaveTripUseCase()

    suspend fun addNewTrip(id: String, trip: TripCall): Response<String> {
        return saveTripUseCase(id, trip)
    }


}