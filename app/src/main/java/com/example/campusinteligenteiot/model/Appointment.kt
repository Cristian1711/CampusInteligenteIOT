package com.example.campusinteligenteiot.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.util.ArrayList

class Appointment(var name: String, date: LocalDate, time: String, userId: String) {
    var date: LocalDate
    var time: String
    var userId: String

    companion object {
        var appointmentList: ArrayList<Appointment> = ArrayList()
        @RequiresApi(Build.VERSION_CODES.O)
        fun appointmentsForDate(date: LocalDate?): ArrayList<Appointment?>? {
            val appointments: ArrayList<Appointment?> = ArrayList()
            for (appointment in appointmentList) {
                if (appointment.date.equals(date)) appointments.add(appointment)
            }
            return appointments
        }
    }

    init {
        this.date = date
        this.time = time
        this.userId = userId
    }
}