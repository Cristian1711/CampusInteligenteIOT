package com.example.campusinteligenteiot.usecases

import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository

class GetUsersUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(): List<UsersResponse>? = repository.getAllUsers()
}