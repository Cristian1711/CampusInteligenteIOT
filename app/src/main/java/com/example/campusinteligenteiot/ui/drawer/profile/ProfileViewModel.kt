package com.example.campusinteligenteiot.ui.drawer.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.image.GetImageUseCase

class ProfileViewModel : ViewModel() {

    private var userRepository = UserRepository()
    var getImageUseCase = GetImageUseCase()
    val uri: MutableLiveData<Uri>? = null

    suspend fun getImageFromStorage(media: String?): MutableLiveData<Uri> {
        getImageUseCase(media).observeForever{
            uri?.value = it
        }
        return uri!!
    }
}
