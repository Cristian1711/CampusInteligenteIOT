package com.example.campusinteligenteiot.api.network.trip

import com.example.campusinteligenteiot.api.network.event.EventApiClient

import android.media.metrics.Event
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.campusinteligenteiot.api.config.RetrofitBuilder
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.trip.TripCall
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.network.product.ProductApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class TripService {

    private val retrofit = RetrofitBuilder.getRetrofit()

    suspend fun getTripById(id: String): TripResponse {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(TripApiClient::class.java).getTripById(id)
            response
        }
    }

    suspend fun getTripByIdLive(id: String): MutableLiveData<TripResponse> {
        var mutableData = MutableLiveData<TripResponse>()
        return withContext(Dispatchers.IO){
            val response = (retrofit.create(TripApiClient::class.java).getTripById(id))
            mutableData.postValue(response)
            mutableData
        }
    }

    suspend fun getAllTrips(): LiveData<MutableList<TripResponse>>?{
        val mutableData = MutableLiveData<MutableList<TripResponse>>()
        var dataList: MutableList<TripResponse>
        return withContext(Dispatchers.IO){
            dataList = (retrofit.create(TripApiClient::class.java).getAllTrips()) as MutableList<TripResponse>
            mutableData.postValue(dataList)
            mutableData
        }
    }

    suspend fun saveTrip(trip: TripCall, id: String) : Response<String> {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(TripApiClient::class.java).saveTrip(trip, id)
            println("he conseguido una respuesta")
            response
        }
    }

    suspend fun deleteTrip(id: String) : TripResponse {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(TripApiClient::class.java).deleteTrip(id)
            println("el evento eliminado")
            response.body()!!
        }
    }
}