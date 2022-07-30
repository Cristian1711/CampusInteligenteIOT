package com.example.campusinteligenteiot.usecases.schedule

import com.example.campusinteligenteiot.api.model.schedule.ScheduleResponse
import com.example.campusinteligenteiot.repository.ScheduleRepository


class GetFullScheduleUseCase {

    private val repository = ScheduleRepository()

    suspend operator fun invoke(): List<ScheduleResponse>? = repository.getFullSchedule()
}