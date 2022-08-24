package com.example.campusinteligenteiot.ui.home.events.adapter.events

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse

class EventAdapter(private val user: UsersResponse, private val context: Context): RecyclerView.Adapter<EventViewHolder> (){

    private var eventMutableList = mutableListOf<EventResponse>()
    private var oldEventMutableList = mutableListOf<EventResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EventViewHolder(layoutInflater.inflate(R.layout.item_event, parent, false), context)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = eventMutableList[position]
        holder.render(item, user)
    }

    override fun getItemCount(): Int = eventMutableList.size

    fun setEventList(data: MutableList<EventResponse>) {
        eventMutableList = data
        oldEventMutableList = eventMutableList
    }
}