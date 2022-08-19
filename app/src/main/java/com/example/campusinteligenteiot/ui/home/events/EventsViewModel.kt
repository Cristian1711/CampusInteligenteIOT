package com.example.campusinteligenteiot.ui.home.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.usecases.event.GetAllEventsUseCase

class EventsViewModel : ViewModel() {
    val getAllEventsUseCase = GetAllEventsUseCase()

    suspend fun getAllEvents(): LiveData<MutableList<EventResponse>> {
        val mutableData = MutableLiveData<MutableList<EventResponse>>()
        getAllEventsUseCase()?.observeForever{ eventList ->
            mutableData.value = eventList
        }
        return mutableData
    }
}