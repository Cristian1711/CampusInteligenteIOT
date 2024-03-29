package com.example.campusinteligenteiot.ui.home.car.passenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.trip.TripCall
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.trip.DeleteTripUseCase
import com.example.campusinteligenteiot.usecases.trip.GetAllTripsUseCase
import com.example.campusinteligenteiot.usecases.trip.SaveTripUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase

class CarPassengerViewModel : ViewModel() {
    val searchUserUseCase = SearchUserUseCase()
    val getAllTripsUseCase = GetAllTripsUseCase()
    val deleteTripUseCase = DeleteTripUseCase()
    val saveTripUseCase = SaveTripUseCase()

    suspend fun getAllTrips(): LiveData<MutableList<TripResponse>> {
        val mutableData = MutableLiveData<MutableList<TripResponse>>()
        getAllTripsUseCase()?.observeForever{ tripList ->
            mutableData.value = tripList
        }
        return mutableData
    }

    suspend fun getUser(id: String): UsersResponse?{
        return searchUserUseCase(id)
    }

    suspend fun deleteTrip(id: String) : TripResponse {
        return deleteTripUseCase(id)
    }

    suspend fun saveTrip(id: String, trip: TripCall){
        saveTripUseCase(id, trip)
    }
}