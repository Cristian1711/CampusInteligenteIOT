package com.example.campusinteligenteiot.ui.drawer.chats

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.model.User
import com.example.campusinteligenteiot.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChannelsViewModel : ViewModel() {

    private var userRepository = UserRepository()
    var user = User()

    suspend fun getUser(): User{
        val uid = Firebase.auth.currentUser?.uid as String
        return userRepository.getSingleUser(uid)
    }
}