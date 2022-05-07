package com.example.campusinteligenteiot.api.network

import com.example.campusinteligenteiot.api.config.RetrofitBuilder
import com.example.campusinteligenteiot.api.model.UsersResponse
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class UserService {

    private val retrofit = RetrofitBuilder.getRetrofit()

    suspend fun searchUserById(id: String):UsersResponse{
        return withContext(Dispatchers.IO){
            val response = retrofit.create(UserApiClient::class.java).getUserById(id)
            response.body()!!
        }
    }

    suspend fun getAllUsers():List<UsersResponse>{
        return withContext(Dispatchers.IO){
            val response = retrofit.create(UserApiClient::class.java).getAllUsers()
            println("El body de la respuesta")
            println(response.body())
            response.body() ?: emptyList()
        }
    }
}