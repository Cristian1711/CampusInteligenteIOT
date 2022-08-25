package com.example.campusinteligenteiot.ui.authentication.splash

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.campusinteligenteiot.common.SingleLiveEvent
import com.example.campusinteligenteiot.ui.home.HomeActivity
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase


class CarViewModel() : ViewModel() {

    private val finishSLE = SingleLiveEvent<Boolean>()
    val searchUserUseCase = SearchUserUseCase()
    val finishLD: LiveData<Boolean> = finishSLE
    lateinit var currentUser: UsersResponse
    fun resume() {
        Looper.myLooper()?.also {
            Handler(it).postDelayed({
                finishSLE.value = (currentUser.driver)
            },2100)
        }
    }

    fun setUser(user: UsersResponse){
        currentUser = user
    }

    suspend fun getUser(id: String): UsersResponse{
        return searchUserUseCase(id)!!
    }

}