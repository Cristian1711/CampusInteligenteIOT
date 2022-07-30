package com.example.campusinteligenteiot.ui.drawer.friends.profile

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase

class FriendsProfileViewModel : ViewModel() {
    var getUserFromLocalUseCase = GetUserFromLocalUseCase()

    suspend fun getUserFromLocal(id:String): UsersResponse?{
        return getUserFromLocalUseCase(id)
    }
}