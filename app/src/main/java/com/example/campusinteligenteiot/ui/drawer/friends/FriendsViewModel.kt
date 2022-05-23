package com.example.campusinteligenteiot.ui.drawer.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.usecases.GetFriendsFromUserUseCase
import com.example.campusinteligenteiot.usecases.GetUserFromLocalUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendsViewModel : ViewModel() {
    private var userRepository = UserRepository()
    var getFriendsFromUserUseCase = GetFriendsFromUserUseCase()

    suspend fun getFriendsFromUser(user: UsersResponse): LiveData<MutableList<UsersResponse>> {
        val mutableData = MutableLiveData<MutableList<UsersResponse>>()
        getFriendsFromUserUseCase(user)?.observeForever{ friendsList ->
            mutableData.value = friendsList
        }
        return mutableData
    }

}