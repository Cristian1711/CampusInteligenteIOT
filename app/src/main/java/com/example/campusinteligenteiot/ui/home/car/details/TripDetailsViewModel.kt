package com.example.campusinteligenteiot.ui.home.car.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.trip.GetSingleTripLiveUseCase
import com.example.campusinteligenteiot.usecases.trip.GetSingleTripUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase
import retrofit2.Response

class TripDetailsViewModel : ViewModel() {
    val getSingleTripLiveUseCase = GetSingleTripLiveUseCase()
    val getSingleTripUseCase = GetSingleTripUseCase()
    val searchUserUseCase = SearchUserUseCase()

    suspend fun getTrip(id: String): MutableLiveData<TripResponse> {
        val mutableData = MutableLiveData<TripResponse>()
        getSingleTripLiveUseCase(id).observeForever { trip ->
            mutableData.postValue(trip)
        }
        return mutableData
    }

    suspend fun getSingleTrip(id: String) : TripResponse {
        return getSingleTripUseCase(id)!!
    }

    suspend fun getUser(id: String): UsersResponse {
        return searchUserUseCase(id)!!
    }
}