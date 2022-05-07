package com.example.campusinteligenteiot.ui.home.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.api.network.UserApiClient
import com.example.campusinteligenteiot.model.User
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.GetUserFromLocalUseCase
import com.example.campusinteligenteiot.usecases.GetUsersUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainHomeViewModel : ViewModel() {

    private var userRepository = UserRepository()
    var user = User()
    var getAllUsersUseCase = GetUsersUseCase()
    var getUserFromLocalUseCase = GetUserFromLocalUseCase()
    val resultUsers = MutableLiveData<List<UsersResponse>>()


    fun onCreate(): LiveData<List<UsersResponse>> {
        viewModelScope.launch {
            resultUsers.value = getAllUsersUseCase()!!
        }
        return resultUsers
    }

    suspend fun getUserFromLocal(id : String): UsersResponse{
        return withContext(Dispatchers.IO){
            val user = getUserFromLocalUseCase(id)!!
            user
        }
    }

    suspend fun getId(): String {
        return Firebase.auth.currentUser?.uid as String
    }

}