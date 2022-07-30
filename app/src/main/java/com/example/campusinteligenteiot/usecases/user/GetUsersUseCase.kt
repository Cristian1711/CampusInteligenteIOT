package com.example.campusinteligenteiot.usecases.user

import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository

class GetUsersUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(): List<UsersResponse>? = repository.getAllUsers()
}