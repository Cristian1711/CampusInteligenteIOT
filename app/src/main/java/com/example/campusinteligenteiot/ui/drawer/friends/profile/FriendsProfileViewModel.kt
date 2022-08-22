package com.example.campusinteligenteiot.ui.drawer.friends.profile

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase
import com.example.campusinteligenteiot.usecases.user.SaveUserUseCase

class FriendsProfileViewModel : ViewModel() {
    var getUserFromLocalUseCase = GetUserFromLocalUseCase()
    var saveUserUseCase = SaveUserUseCase()

    suspend fun getUserFromLocal(id:String): UsersResponse?{
        return getUserFromLocalUseCase(id)
    }

    suspend fun updateUserFriends(user: UsersResponse){
        saveUserUseCase(user.id, user)
    }
}