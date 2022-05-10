package com.example.campusinteligenteiot.ui.drawer.profile

import android.content.Context
import android.net.Uri
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusinteligenteiot.api.config.RetrofitBuilder
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.api.network.UserApiClient
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.GetImageUseCase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private var userRepository = UserRepository()
    var getImageUseCase = GetImageUseCase()

    suspend fun getImageFromStorage(media: String?): Uri? {
        return getImageUseCase(media)
    }
}
