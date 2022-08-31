package com.example.campusinteligenteiot.usecases.user

import com.example.campusinteligenteiot.repository.UserRepository

class SaveFriendsUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(idFriends: ArrayList<String>, id: String) = repository.saveFriends(idFriends, id)
}