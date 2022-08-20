package com.example.campusinteligenteiot.ui.drawer.qr

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.image.GetImageUseCase
import com.example.campusinteligenteiot.usecases.user.SaveUserUseCase

class GenerateQRViewModel : ViewModel() {

    val saveUserUseCase = SaveUserUseCase()

    suspend fun updateFriendList(user: UsersResponse){
        saveUserUseCase(user.id, user)
    }

}