package com.example.campusinteligenteiot.api.network.event

import android.media.metrics.Event
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.campusinteligenteiot.api.config.RetrofitBuilder
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.network.product.ProductApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class EventService {

    private val retrofit = RetrofitBuilder.getRetrofit()

    suspend fun getEventById(id: String): EventResponse {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(EventApiClient::class.java).getEventById(id)
            response
        }
    }

    suspend fun getEventByIdLive(id: String): MutableLiveData<EventResponse> {
        var mutableData = MutableLiveData<EventResponse>()
        return withContext(Dispatchers.IO){
            val response = (retrofit.create(EventApiClient::class.java).getEventById(id))
            mutableData.postValue(response)
            mutableData
        }
    }

    suspend fun getAllEvents(): LiveData<MutableList<EventResponse>>?{
        val mutableData = MutableLiveData<MutableList<EventResponse>>()
        var dataList: MutableList<EventResponse>
        return withContext(Dispatchers.IO){
            dataList = (retrofit.create(EventApiClient::class.java).getAllEvents()) as MutableList<EventResponse>
            mutableData.postValue(dataList)
            mutableData
        }
    }

    suspend fun saveEvent(event: EventCall, id: String) : Response<String> {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(EventApiClient::class.java).saveEvent(event, id)
            response
        }
    }

    suspend fun deleteEvent(id: String) : EventResponse {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(EventApiClient::class.java).deleteEvent(id)
            response.body()!!
        }
    }
}