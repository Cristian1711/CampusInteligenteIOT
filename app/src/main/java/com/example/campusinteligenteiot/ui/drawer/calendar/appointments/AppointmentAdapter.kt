package com.example.campusinteligenteiot.ui.drawer.calendar.appointments

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.model.Appointment
import com.example.campusinteligenteiot.ui.drawer.calendar.utils.CalendarUtils.formattedTime
import java.util.ArrayList

class AppointmentAdapter(private val appointmentsList:List<Appointment?>?) : RecyclerView.Adapter<AppointmentViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AppointmentViewHolder(layoutInflater.inflate(R.layout.appointment_cell, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val item = appointmentsList!![position]
        holder.render(item!!)
    }

    override fun getItemCount(): Int =  appointmentsList!!.size



}