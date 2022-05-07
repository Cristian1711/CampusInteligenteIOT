package com.example.campusinteligenteiot.api.model

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
}