package com.example.campusinteligenteiot.api.network.schedule

import com.example.campusinteligenteiot.api.config.RetrofitBuilder
import com.example.campusinteligenteiot.api.model.schedule.ScheduleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScheduleService {

    private val retrofit = RetrofitBuilder.getRetrofit()

    suspend fun getScheduleById(id: String): ScheduleResponse {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(ScheduleApiClient::class.java).getScheduleById(id)
            response.body()!!
        }
    }

    suspend fun getFullSchedule():List<ScheduleResponse>{
        return withContext(Dispatchers.IO){
            val response = retrofit.create(ScheduleApiClient::class.java).getFullSchedule()
            println(response.body())
            response.body() ?: emptyList()
        }
    }
}