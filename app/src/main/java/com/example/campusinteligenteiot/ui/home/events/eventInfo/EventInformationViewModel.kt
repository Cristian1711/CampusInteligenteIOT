package com.example.campusinteligenteiot.ui.home.events.eventInfo

import android.media.metrics.Event
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.event.GetSingleEventLiveUseCase
import com.example.campusinteligenteiot.usecases.event.GetSingleEventUseCase
import com.example.campusinteligenteiot.usecases.event.SaveEventUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase

class EventInformationViewModel : ViewModel() {

    val getSingleEventLiveUseCase = GetSingleEventLiveUseCase()
    val getSingleEventUseCase = GetSingleEventUseCase()
    val searchUserUseCase = SearchUserUseCase()
    val saveEventUseCase = SaveEventUseCase()

    suspend fun getEvent(id: String): LiveData<EventResponse>{
        val mutableData = MutableLiveData<EventResponse>()
        getSingleEventLiveUseCase(id).observeForever { event ->
            mutableData.value = event
        }
        return mutableData
    }

    suspend fun getSingleEvent(id: String) : EventResponse{
        return getSingleEventUseCase(id)!!
    }

    suspend fun getUser(id: String): UsersResponse{
        return searchUserUseCase(id)!!
    }

    suspend fun saveEvent(id: String, event: EventCall){
        saveEventUseCase(id, event)
    }
}