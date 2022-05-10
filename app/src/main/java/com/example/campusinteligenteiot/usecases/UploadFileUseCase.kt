package com.example.campusinteligenteiot.usecases

import android.graphics.drawable.Drawable
import android.net.Uri
import com.example.campusinteligenteiot.repository.UserRepository

class UploadFileUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(drawable: Drawable) = repository.uploadFile(drawable)
}