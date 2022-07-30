package com.example.campusinteligenteiot.ui.drawer.friends

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class FriendsViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view){

    val friendName = view.findViewById<TextView>(R.id.friendName)
    val friendDegree = view.findViewById<TextView>(R.id.friendDegree)
    val friendImage = view.findViewById<CircleImageView>(R.id.friendImage)

    fun render(friend : UsersResponse){
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(friend.profileImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(friendImage)
        }
        friendName.text = friend.name
        friendDegree.text = friend.collegeDegree
    }
}