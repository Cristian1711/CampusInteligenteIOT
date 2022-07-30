package com.example.campusinteligenteiot.ui.drawer.editprofile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.image.GetImageUseCase
import com.example.campusinteligenteiot.usecases.user.SaveUserUseCase
import retrofit2.Response

class EditProfileViewModel : ViewModel() {
    var saveUserUseCase = SaveUserUseCase()
    var getImageUseCase = GetImageUseCase()

    suspend fun getImageFromStorage(media: String?): MutableLiveData<Uri> {
        return getImageUseCase(media)
    }

    suspend fun saveUser(user: UsersResponse, id:String) : Response<String> {
        return saveUserUseCase(id, user)
    }
}