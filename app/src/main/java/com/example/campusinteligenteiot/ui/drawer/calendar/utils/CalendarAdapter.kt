package com.example.campusinteligenteiot.ui.drawer.calendar.utils

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import java.time.LocalDate
import java.util.ArrayList

class CalendarAdapter(
    private val days: ArrayList<LocalDate>,
    private val onItemListener: OnItemListener
) : RecyclerView.Adapter<CalendarViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        if (days.size > 15) //month view
            layoutParams.height = (parent.height * 0.166666666).toInt() else  // week view
            layoutParams.height = parent.height
        return CalendarViewHolder(view, onItemListener, days)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(@NonNull holder: CalendarViewHolder, position: Int) {
        val date = days[position]
        if (date == null) holder.dayOfMonth.text = "" else {
            holder.dayOfMonth.text = date.dayOfMonth.toString()
            if (date == CalendarUtils.selectedDate) holder.parentView.setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
    }
}