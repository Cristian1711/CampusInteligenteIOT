package com.example.campusinteligenteiot.ui.drawer.editprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.GetImageUseCase
import com.example.campusinteligenteiot.usecases.SaveUserUseCase

class EditProfileViewModel : ViewModel() {
    private var userRepository = UserRepository()
    var saveUserUseCase = SaveUserUseCase()
    var getImageUseCase = GetImageUseCase()

    suspend fun getImageFromStorage(media: String?): Uri? {
        return getImageUseCase(media)
    }

    suspend fun saveUser(id:String, user: UsersResponse){
        userRepository.saveUser(id, user)
    }
}