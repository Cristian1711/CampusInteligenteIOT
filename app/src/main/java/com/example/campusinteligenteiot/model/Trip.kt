package com.example.campusinteligenteiot.model

import java.util.*
import kotlin.collections.ArrayList

data class Trip(var finalPoint: ArrayList<Double>,
                var originPoint: ArrayList<Double>,
                var passengers: ArrayList<String>?,
                var departureDate: String,
                var seats: Int,
                var deleted: Boolean,
                var driver: String,
                var available:Boolean
)