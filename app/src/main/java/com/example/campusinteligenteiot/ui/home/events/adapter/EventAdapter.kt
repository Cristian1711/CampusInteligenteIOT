package com.example.campusinteligenteiot.ui.home.events.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventResponse

class EventAdapter(private val eventList: List<EventResponse>, private val context: Context,
                   private val onClickListener:(EventResponse) -> Unit): RecyclerView.Adapter<EventViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EventViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false), context)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = eventList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int = eventList.size
}