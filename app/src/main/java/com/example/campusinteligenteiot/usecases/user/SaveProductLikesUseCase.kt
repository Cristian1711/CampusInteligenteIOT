package com.example.campusinteligenteiot.usecases.user

import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository

class SaveProductLikesUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(idProducts: ArrayList<String>, id: String) = repository.saveProductLikes(idProducts, id)
}