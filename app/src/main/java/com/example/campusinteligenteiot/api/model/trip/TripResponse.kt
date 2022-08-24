package com.example.campusinteligenteiot.api.model.event

import com.google.gson.annotations.SerializedName
import java.util.*

data class TripResponse (
    @SerializedName("id") var id: String,
    @SerializedName("finalPoint") var finalPoint: ArrayList<String>,
    @SerializedName("originPoint") var originPoint: ArrayList<String>,
    @SerializedName("passengers") var passengers: ArrayList<String>,
    @SerializedName("departureDate") var departureDate: Date,
    @SerializedName("seats") var seats:Int
)
