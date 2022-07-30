package com.example.campusinteligenteiot.usecases.user

import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository

class SearchUserUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(id:String): UsersResponse? = repository.searchUserById(id)
}