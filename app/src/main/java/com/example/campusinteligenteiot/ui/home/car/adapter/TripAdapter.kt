package com.example.campusinteligenteiot.ui.home.car.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.ui.home.events.adapter.events.EventViewHolder

class TripAdapter(private val user: UsersResponse, private val context: Context,
                  private val onClickDelete:(Int) -> Unit): RecyclerView.Adapter<TripViewHolder> (){

    private var tripMutableList = mutableListOf<TripResponse>()
    private var oldTripMutableList = mutableListOf<TripResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TripViewHolder(layoutInflater.inflate(R.layout.trip_item, parent, false), context)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val item = tripMutableList[position]
        holder.render(item, user, onClickDelete)
    }

    override fun getItemCount(): Int = tripMutableList.size

    fun setTripList(data: MutableList<TripResponse>) {
        tripMutableList = data
        oldTripMutableList = tripMutableList
    }

    fun getTripList(): MutableList<TripResponse>{
        return tripMutableList
    }

    fun filterTripListByDriver(userId: String){
        tripMutableList = (oldTripMutableList.filter { it.driver == userId && !it.deleted && it.available}) as MutableList<TripResponse>
    }

}