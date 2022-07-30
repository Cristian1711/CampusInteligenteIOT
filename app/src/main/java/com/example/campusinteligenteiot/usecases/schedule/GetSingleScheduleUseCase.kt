package com.example.campusinteligenteiot.usecases.schedule

import com.example.campusinteligenteiot.api.model.schedule.ScheduleResponse
import com.example.campusinteligenteiot.repository.ScheduleRepository


class GetSingleScheduleUseCase {

    private val repository = ScheduleRepository()

    suspend operator fun invoke(id:String): ScheduleResponse? = repository.getScheduleById(id)
}