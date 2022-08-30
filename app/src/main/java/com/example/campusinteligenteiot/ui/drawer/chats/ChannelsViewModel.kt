package com.example.campusinteligenteiot.ui.drawer.chats

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.model.User
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChannelsViewModel : ViewModel() {

    val searchUserUseCase = SearchUserUseCase()

    suspend fun getUser(id: String): UsersResponse{
        return searchUserUseCase(id)!!
    }
}