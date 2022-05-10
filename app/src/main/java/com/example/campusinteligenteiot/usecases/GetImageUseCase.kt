package com.example.campusinteligenteiot.usecases

import android.net.Uri
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository

class GetImageUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(media: String?): Uri? = repository.getImageFromStorage(media)
}