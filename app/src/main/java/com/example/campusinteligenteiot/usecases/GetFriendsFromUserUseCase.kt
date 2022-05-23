package com.example.campusinteligenteiot.usecases

import androidx.lifecycle.LiveData
import com.example.campusinteligenteiot.api.model.UserProvider
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository

class GetFriendsFromUserUseCase {

    private val repository = UserRepository()
    private val userProvider = UserProvider()

    suspend operator fun invoke(user : UsersResponse): LiveData<MutableList<UsersResponse>>? {
        val users = UserProvider.users
        if(!users.isNullOrEmpty()) {
            println("NO ESTA VACIA LA LISTA DE USUARIOS")
            return userProvider.getFriendsFromUser(user, users)
        }
        println("LA LISTA DE USUARIOS ESTA VACIA")
        return null
    }
}