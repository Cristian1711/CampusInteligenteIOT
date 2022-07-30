package com.example.campusinteligenteiot.ui.authentication.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.campusinteligenteiot.usecases.user.AuthUserUseCase

class SignUpVMFactory(private val authUserUseCase: AuthUserUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AuthUserUseCase::class.java).newInstance(authUserUseCase)
    }

}