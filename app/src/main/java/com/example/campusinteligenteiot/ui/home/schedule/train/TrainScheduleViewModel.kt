package com.example.campusinteligenteiot.ui.home.schedule.train

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.schedule.ScheduleResponse
import com.example.campusinteligenteiot.usecases.schedule.GetFullScheduleUseCase
import com.example.campusinteligenteiot.usecases.schedule.GetSingleScheduleUseCase

class TrainScheduleViewModel : ViewModel() {
    var getSingleScheduleUseCase = GetSingleScheduleUseCase()
    val getFullScheduleUseCase = GetFullScheduleUseCase()

    suspend fun getSingleSchedule(idSchedule: String) : ScheduleResponse? {
        return getSingleScheduleUseCase(idSchedule)
    }

    suspend fun getFullSchedule() : List<ScheduleResponse>? {
        return getFullScheduleUseCase()
    }

}