package com.example.campusinteligenteiot.usecases

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import com.example.campusinteligenteiot.common.Resource
import com.example.campusinteligenteiot.common.SingleLiveEvent
import com.example.campusinteligenteiot.repository.UserRepository

class AuthUserUseCase(private val userRepository: UserRepository) {

    fun firebaseDefaultAuthSignUp(email: String, passwd: String, repeatedPasswd: String) : Resource<FirebaseUser?>? {
        return userRepository.signUpDefault(email,passwd,repeatedPasswd)
    }

    fun firebaseDefaultAuthSignIn(email: String, passwd: String) : Resource<FirebaseUser?>{
        return userRepository.signInDefault(email,passwd)
    }

    fun firebaseDefaultGetCurrentUser() : com.example.campusinteligenteiot.model.User{
        return userRepository.getCurrentUser()
    }

}