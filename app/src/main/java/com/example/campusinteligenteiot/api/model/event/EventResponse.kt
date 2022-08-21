package com.example.campusinteligenteiot.api.model.event

import com.google.gson.annotations.SerializedName
import java.util.*

data class EventResponse (
    @SerializedName("id") var id: String,
    @SerializedName("assistants") var assistants: ArrayList<String>,
    @SerializedName("attendances") var attendances: ArrayList<String>,
    @SerializedName("eventDate") var eventDate: Date,
    @SerializedName("description") var description: String,
    @SerializedName("eventImage") var eventImage: String,
    @SerializedName("eventTitle") var eventTitle: String,
    @SerializedName("eventPlace") var eventPlace: String,
    @SerializedName("suggested") var suggested: Boolean
    )
