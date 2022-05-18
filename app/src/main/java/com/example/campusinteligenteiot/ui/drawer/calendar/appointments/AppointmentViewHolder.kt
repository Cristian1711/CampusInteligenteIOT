package com.example.campusinteligenteiot.ui.drawer.calendar.appointments

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.model.Appointment
import com.example.campusinteligenteiot.ui.drawer.calendar.utils.CalendarUtils.monthYearFromDate

class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val appointmentName = view.findViewById<TextView>(R.id.appointmentName)
    val appointmentDay = view.findViewById<TextView>(R.id.appointmentDay)
    val appointmentHour = view.findViewById<TextView>(R.id.appointmentHour)

    @RequiresApi(Build.VERSION_CODES.O)
    fun render(appointment : Appointment){
        appointmentName.text = appointment.name
        appointmentDay.text = monthYearFromDate(appointment.date)
        appointmentHour.text = appointment.time.toString()
    }
}