package com.example.campusinteligenteiot.ui.drawer.editprofile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.GetImageUseCase
import com.example.campusinteligenteiot.usecases.SaveUserUseCase
import retrofit2.Call
import retrofit2.Response

class EditProfileViewModel : ViewModel() {
    private var userRepository = UserRepository()
    var saveUserUseCase = SaveUserUseCase()
    var getImageUseCase = GetImageUseCase()

    suspend fun getImageFromStorage(media: String?): MutableLiveData<Uri> {
        return getImageUseCase(media)
    }

    suspend fun saveUser(user: UsersResponse, id:String) : Response<String> {
        return userRepository.saveUser(user, id)
    }
}