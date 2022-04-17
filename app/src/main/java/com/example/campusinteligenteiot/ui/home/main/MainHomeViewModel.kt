package com.example.campusinteligenteiot.ui.home.main

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.model.User
import com.example.campusinteligenteiot.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainHomeViewModel : ViewModel() {

    private var userRepository = UserRepository()
    var user = User()

    suspend fun getUser(): User {
        val uid = Firebase.auth.currentUser?.uid as String

        println("USUARIO")
        println(uid)
        return userRepository.getSingleUser(uid)
    }
}