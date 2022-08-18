package com.example.campusinteligenteiot.ui.home.shop.detail

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase

class SellerProfileViewModel : ViewModel() {
    var getUserFromLocalUseCase = GetUserFromLocalUseCase()

    suspend fun getUserFromLocal(id:String): UsersResponse?{
        return getUserFromLocalUseCase(id)
    }
}