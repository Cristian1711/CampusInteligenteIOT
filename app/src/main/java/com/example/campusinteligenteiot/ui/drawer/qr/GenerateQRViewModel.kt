package com.example.campusinteligenteiot.ui.drawer.qr

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.image.GetImageUseCase
import com.example.campusinteligenteiot.usecases.user.SaveFriendsUseCase
import com.example.campusinteligenteiot.usecases.user.SaveUserUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase

class GenerateQRViewModel : ViewModel() {

    val saveUserUseCase = SaveUserUseCase()
    val searchUserUseCase = SearchUserUseCase()
    val saveFriendsUseCase = SaveFriendsUseCase()

    suspend fun updateFriendList(user: UsersResponse){
        saveUserUseCase(user.id, user)
    }

    suspend fun getUser(id: String): UsersResponse{
        return searchUserUseCase(id)!!
    }

    suspend fun saveFriends(idFriends: ArrayList<String>, id: String){
        saveFriendsUseCase(idFriends, id)
    }

}