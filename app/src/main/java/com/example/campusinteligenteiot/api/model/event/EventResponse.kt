package com.example.campusinteligenteiot.api.model.event

import com.google.gson.annotations.SerializedName
import java.util.ArrayList
import java.util.HashMap

data class EventResponse (
    @SerializedName("id") var id: String,
    @SerializedName("assistants") var assistants: ArrayList<String>,
    @SerializedName("attendances") var attendances: Int,
    @SerializedName("eventDate") var eventDate: String,
    @SerializedName("description") var description: String,
    @SerializedName("eventImage") var eventImage: String
    )
