package com.example.campusinteligenteiot.ui.drawer.friends.profile

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase
import com.example.campusinteligenteiot.usecases.user.SaveFriendsUseCase
import com.example.campusinteligenteiot.usecases.user.SaveUserUseCase

class FriendsProfileViewModel : ViewModel() {
    var getUserFromLocalUseCase = GetUserFromLocalUseCase()
    var saveUserUseCase = SaveUserUseCase()
    var saveFriendsUseCase = SaveFriendsUseCase()

    fun getUserFromLocal(id:String): UsersResponse?{
        return getUserFromLocalUseCase(id)
    }

    suspend fun updateUserFriends(user: UsersResponse){
        saveUserUseCase(user.id, user)
    }

    suspend fun saveFriends(idFriends: ArrayList<String>, id: String){
        saveFriendsUseCase(idFriends, id)
    }
}