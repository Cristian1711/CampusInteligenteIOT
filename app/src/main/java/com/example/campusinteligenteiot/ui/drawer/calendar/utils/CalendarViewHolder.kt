package com.example.campusinteligenteiot.ui.drawer.calendar.utils

import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import java.time.LocalDate
import java.util.ArrayList

class CalendarViewHolder(
    @NonNull itemView: View,
    onItemListener: CalendarAdapter.OnItemListener,
    days: ArrayList<LocalDate>
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val days: ArrayList<LocalDate>
    val parentView: View
    val dayOfMonth: TextView
    private val onItemListener: CalendarAdapter.OnItemListener

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])
    }

    init {
        parentView = itemView.findViewById(R.id.parentView)
        dayOfMonth = itemView.findViewById(R.id.cellDayText)
        this.onItemListener = onItemListener
        itemView.setOnClickListener(this)
        this.days = days
    }
}