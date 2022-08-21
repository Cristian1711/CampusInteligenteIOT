package com.example.campusinteligenteiot.ui.home.events.suggests

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.usecases.event.SaveEventUseCase
import retrofit2.Response

class NewEventSuggestViewModel : ViewModel() {
    val saveEventUseCase = SaveEventUseCase()

    suspend fun saveSuggestion(id:String, event: EventCall): Response<String> {
        return saveEventUseCase(id, event)
    }

}