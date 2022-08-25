package com.example.campusinteligenteiot.ui.drawer.friends

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ItemMyProductBinding
import com.example.campusinteligenteiot.databinding.UserItemBinding
import com.example.campusinteligenteiot.usecases.user.SaveUserUseCase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UsersViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view){

    val binding = UserItemBinding.bind(view)
    val myView = view
    private val client = ChatClient.instance()
    lateinit var friendUser: UsersResponse
    lateinit var currentUser: UsersResponse
    val updateUserFriends = SaveUserUseCase()

    fun render(friend : UsersResponse, user: UsersResponse){
        friendUser = friend
        currentUser = user
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(friend.profileImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(binding.friendImage)
        }

        if(user.friends.contains(friend.id)){
            binding.addFriendButton.visibility = GONE
        }
        else{
            binding.addFriendButton.visibility = VISIBLE
        }

        binding.friendName.text = friend.name
        binding.friendDegree.text = friend.collegeDegree

        binding.showProfileButton.setOnClickListener{
            val bundle = bundleOf(
                "userId" to friend.id
            )
            val navController = Navigation.findNavController(myView)
            navController.navigate(R.id.action_eventInformationFragment_to_friendsProfileFragment2, bundle)
        }

        binding.chatButton.setOnClickListener{
            createChat(user.userName, friend.userName)
            val navController = Navigation.findNavController(myView)
            Handler().postDelayed({
                navController.navigate(R.id.action_eventInformationFragment_to_channelsFragment)
            }, 1500)
        }

        binding.addFriendButton.setOnClickListener{
            showAlertDialog()
        }

    }

    fun createChat(currentUserUserName: String, sellerUserName: String) {
        client.createChannel(
            channelType = "messaging",
            members = listOf(currentUserUserName,sellerUserName)
        ).enqueue { result ->
            if (result.isSuccess) {
                val channel = result.data()
            } else {
                println(context.getString(R.string.error_chat))
                // Handle result.error()
            }
        }
    }

    fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle(context.getString(R.string.friend_request))
            setMessage(context.getString(R.string.question_2)+ friendUser.userName + "?")
            setPositiveButton("Sí") { dialog: DialogInterface, _: Int ->
                currentUser.friends.add(friendUser.id)
                GlobalScope.launch(Dispatchers.Main){
                    updateUserFriends(currentUser.id, currentUser)
                    if(currentUser.friends.contains(friendUser.id) && friendUser.friends.contains(currentUser.id)){
                        createChat(currentUser.userName, friendUser.userName)
                    }
                    dialog.dismiss()
                }
            }
            setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
        }.create().show()
    }
}