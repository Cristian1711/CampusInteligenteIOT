package com.example.campusinteligenteiot.repository

import com.example.campusinteligenteiot.api.model.schedule.ScheduleResponse
import com.example.campusinteligenteiot.api.network.schedule.ScheduleService


class ScheduleRepository {

    private val api = ScheduleService()

    suspend fun getFullSchedule(): List<ScheduleResponse> {
        return api.getFullSchedule()
    }

    suspend fun getScheduleById(id: String): ScheduleResponse {
        return api.getScheduleById(id)
    }
}