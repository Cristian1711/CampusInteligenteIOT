package com.example.campusinteligenteiot.usecases

import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository

class SearchUserUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(id:String):UsersResponse? = repository.searchUserById(id)
}