package com.example.campusinteligenteiot.api.model.schedule

import com.google.gson.annotations.SerializedName
import java.util.ArrayList
import java.util.HashMap

data class ScheduleResponse (
    @SerializedName("departures") var departures:ArrayList<String>? = null,
    @SerializedName("arrivals") var arrivals:ArrayList<String>? = null,
    )