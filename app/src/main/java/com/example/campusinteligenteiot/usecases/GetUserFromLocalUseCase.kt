package com.example.campusinteligenteiot.usecases

import com.example.campusinteligenteiot.api.model.UserProvider
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository

class GetUserFromLocalUseCase {

    private val repository = UserRepository()
    private val userProvider = UserProvider()

    suspend operator fun invoke(id : String): UsersResponse? {
        val users = UserProvider.users
        var user : UsersResponse
        if(!users.isNullOrEmpty()){
            println("NO ESTA VACIA LA LISTA DE USUARIOS")
            user = userProvider.searchById(id, users)!!
            return user
        }
        println("LA LISTA DE USUARIOS ESTA VACIA")
        return null
    }
}