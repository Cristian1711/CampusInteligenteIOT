package com.example.campusinteligenteiot.usecases.user

import androidx.lifecycle.LiveData
import com.example.campusinteligenteiot.api.model.user.UserProvider
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository

class GetFriendsFromUserUseCase {

    private val repository = UserRepository()
    private val userProvider = UserProvider()

    suspend operator fun invoke(user : UsersResponse): LiveData<MutableList<UsersResponse>>? {
        val users = UserProvider.users
        if(!users.isNullOrEmpty()) {
            return userProvider.getFriendsFromUser(user, users)
        }
        return null
    }
}