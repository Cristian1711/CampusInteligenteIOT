package com.example.campusinteligenteiot.ui.home.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.model.User
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.event.GetSingleEventUseCase
import com.example.campusinteligenteiot.usecases.event.SaveEventUseCase
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase
import com.example.campusinteligenteiot.usecases.user.GetUsersUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainHomeViewModel : ViewModel() {

    var user = User()
    var getAllUsersUseCase = GetUsersUseCase()
    val searchUserUseCase = SearchUserUseCase()
    var getUserFromLocalUseCase = GetUserFromLocalUseCase()
    val resultUsers = MutableLiveData<List<UsersResponse>>()
    val saveEventUseCase = SaveEventUseCase()
    val getEventUseCase = GetSingleEventUseCase()


    fun onCreate(): LiveData<List<UsersResponse>> {
        viewModelScope.launch {
            resultUsers.value = getAllUsersUseCase()!!
        }
        return resultUsers
    }

    suspend fun getUserFromLocal(id : String): UsersResponse {
        return withContext(Dispatchers.IO){
            val user = getUserFromLocalUseCase(id)!!
            user
        }
    }

    suspend fun getEvent(id: String) : EventResponse{
        return getEventUseCase(id)!!
    }

    suspend fun getId(): String {
        return Firebase.auth.currentUser?.uid as String
    }

    suspend fun getSingleUser(id: String): UsersResponse{
        return searchUserUseCase(id)!!
    }

    suspend fun saveEvent(id: String, event: EventCall){
        saveEventUseCase(id, event)
    }

}