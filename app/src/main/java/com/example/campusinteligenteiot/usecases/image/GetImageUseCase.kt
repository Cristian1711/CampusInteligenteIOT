package com.example.campusinteligenteiot.usecases.image

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.campusinteligenteiot.repository.UserRepository

class GetImageUseCase {

    private val repository = UserRepository()

    suspend operator fun invoke(media: String?) : MutableLiveData<Uri> = repository.getImageFromStorage(media)
}