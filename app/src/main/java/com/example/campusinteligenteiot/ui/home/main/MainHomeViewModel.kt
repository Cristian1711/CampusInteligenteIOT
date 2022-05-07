package com.example.campusinteligenteiot.ui.home.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusinteligenteiot.model.User
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.GetUsersUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainHomeViewModel : ViewModel() {

    private var userRepository = UserRepository()
    var user = User()
    var getAllUsersUseCase = GetUsersUseCase()


    fun onCreate() {
        viewModelScope.launch {
            val result = getAllUsersUseCase()

            if (!result.isNullOrEmpty()){
                println("TODO PERFECTO")
            }
            else{
                println("NO HA FUNCIONADO BIEN")
            }
        }
    }

    suspend fun getUser(): User {
        val uid = Firebase.auth.currentUser?.uid as String

        println("USUARIO")
        println(uid)
        return userRepository.getSingleUser(uid)
    }

}