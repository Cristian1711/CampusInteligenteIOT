package com.example.campusinteligenteiot.usecases.user

import com.example.campusinteligenteiot.api.model.user.UserProvider
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository

class GetUserFromLocalUseCase {

    private val repository = UserRepository()
    private val userProvider = UserProvider()

    operator fun invoke(id : String): UsersResponse? {
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