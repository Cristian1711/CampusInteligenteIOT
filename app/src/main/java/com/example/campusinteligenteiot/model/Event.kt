package com.example.campusinteligenteiot.model

import java.util.*
import kotlin.collections.ArrayList

data class Event(val idEvent: String,
                 val assistants: ArrayList<String>,
                 val attendances: ArrayList<String>,
                 val eventDate: Date,
                 val description: String,
                 val eventImage: String? = null
                 )