package com.example.campusinteligenteiot.ui.drawer.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse

class UsersAdapter(private val user: UsersResponse, private val context: Context) :
    RecyclerView.Adapter<UsersViewHolder>(){

    private var assistantsMutableList = mutableListOf<UsersResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsersViewHolder(layoutInflater.inflate(R.layout.user_item, parent, false), context)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = assistantsMutableList[position]
        holder.render(item, user)
    }

    override fun getItemCount(): Int =  assistantsMutableList.size

    fun setAssistantsList(data: MutableList<UsersResponse>) {
        assistantsMutableList = data
    }
}