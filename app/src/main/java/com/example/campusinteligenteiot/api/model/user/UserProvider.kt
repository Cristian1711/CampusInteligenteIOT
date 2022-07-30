package com.example.campusinteligenteiot.api.model.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UserProvider {
    companion object{
        var users:List<UsersResponse> = emptyList()
    }

    fun searchById(id : String, users : List<UsersResponse>): UsersResponse?{
        for(user in users){
            if((user.id) == id){
                return user
            }
        }
        return null
    }

    fun getFriendsFromUser(user: UsersResponse, users: List<UsersResponse>) : LiveData<MutableList<UsersResponse>> {
        val mutableList = MutableLiveData<MutableList<UsersResponse>>()
        val dataList = mutableListOf<UsersResponse>()

        if(user.friends != null){
            for(user in user.friends){
                val friend = searchById(user, users)
                dataList.add(friend!!)
            }
        }
        mutableList.value = dataList
        return mutableList
    }
}