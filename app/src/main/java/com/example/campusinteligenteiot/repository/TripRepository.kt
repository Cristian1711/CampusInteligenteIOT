package com.example.campusinteligenteiot.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.trip.TripCall
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.network.event.EventService
import com.example.campusinteligenteiot.api.network.trip.TripService
import retrofit2.Response

class TripRepository {

    private val api = TripService()

    suspend fun getTripById(id: String): TripResponse {
        return api.getTripById(id)
    }

    suspend fun getTripByIdLive(id: String): MutableLiveData<TripResponse> {
        return api.getTripByIdLive(id)
    }

    suspend fun getAllTrips(): LiveData<MutableList<TripResponse>>? {
        return api.getAllTrips()
    }

    suspend fun saveTrip(trip: TripCall, id: String) : Response<String> {
        return api.saveTrip(trip, id)
    }

    suspend fun deleteTrip(id: String) : TripResponse{
        return api.deleteTrip(id)
    }
}