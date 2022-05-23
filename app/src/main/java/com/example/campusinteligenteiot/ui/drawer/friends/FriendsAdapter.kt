package com.example.campusinteligenteiot.ui.drawer.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.ui.drawer.calendar.appointments.AppointmentViewHolder

class FriendsAdapter(private val context: Context) :
    RecyclerView.Adapter<FriendsViewHolder>(){

    private var friendsMutableList = mutableListOf<UsersResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FriendsViewHolder(layoutInflater.inflate(R.layout.friend_item, parent, false), context)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val item = friendsMutableList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int =  friendsMutableList.size

    fun setFriendList(data: MutableList<UsersResponse>) {
        friendsMutableList = data
    }
}