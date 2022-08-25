package com.example.campusinteligenteiot.api.model.trip

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

class TripCall (
    var finalPoint: ArrayList<Double>,
    var originPoint: ArrayList<Double>,
    var passengers: ArrayList<String>?,
    var departureDate: String,
    var seats: Int,
    var deleted: Boolean,
    var driver: String,
    var available:Boolean
)