package com.example.campusinteligenteiot.ui.drawer.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse

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

        holder.itemView.setOnClickListener{
            val bundle = bundleOf(
                "userId" to item.id
            )
            val navController = Navigation.findNavController(holder.itemView)
            navController!!.navigate(R.id.action_friendsFragment_to_friendsProfileFragment2, bundle)
        }
    }

    override fun getItemCount(): Int =  friendsMutableList.size

    fun setFriendList(data: MutableList<UsersResponse>) {
        friendsMutableList = data
    }
}