package com.example.campusinteligenteiot.api.model.event

import com.google.firebase.Timestamp
import java.util.*

class EventCall(
    var assistants: ArrayList<String>? = null,
    var attendances: ArrayList<String>? = null,
    var eventDate: String,
    var description: String,
    var eventImage: String? = null,
    var eventTitle: String,
    var eventPlace: String,
    var suggested: Boolean
)