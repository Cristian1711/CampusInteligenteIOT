package com.example.campusinteligenteiot.ui.authentication.registerdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.common.SingleLiveEvent

class RegisterNameViewModel: ViewModel() {

    private val errorSLE = SingleLiveEvent<Int>()
    private val successSLE = SingleLiveEvent<Int>()
    val errorLD: LiveData<Int> = errorSLE
    val successLD: LiveData<Int> = successSLE

    fun setValues(name: String, surname: String, username: String) {
        if (name.isEmpty()) {
            errorSLE.value = R.string.register_empty_name
            return
        }
        else if (surname.isEmpty()) {
            errorSLE.value = R.string.register_empty_surname
            return
        }
        else if (username.isBlank()) {
            errorSLE.value = R.string.register_empty_username
            return
        }
        else {
            successSLE.value = R.string.register_success
        }
    }

}