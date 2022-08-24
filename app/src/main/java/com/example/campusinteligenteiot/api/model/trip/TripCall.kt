package com.example.campusinteligenteiot.api.model.trip

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

class TripCall (
    var finalPoint: ArrayList<String>,
    var originPoint: ArrayList<String>,
    var passengers: ArrayList<String>,
    var departureDate: String,
    var seats: Int,
    var deleted: Boolean,
    var driver: String
)