package com.example.campusinteligenteiot.ui.drawer.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.user.GetFriendsFromUserUseCase

class FriendsViewModel : ViewModel() {
    var getFriendsFromUserUseCase = GetFriendsFromUserUseCase()

    suspend fun getFriendsFromUser(user: UsersResponse): LiveData<MutableList<UsersResponse>> {
        val mutableData = MutableLiveData<MutableList<UsersResponse>>()
        getFriendsFromUserUseCase(user)?.observeForever{ friendsList ->
            mutableData.value = friendsList
        }
        return mutableData
    }

}