package com.example.campusinteligenteiot.api.network

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.example.campusinteligenteiot.api.config.RetrofitBuilder
import com.example.campusinteligenteiot.api.model.UsersResponse
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    suspend fun saveUser(user: UsersResponse, id: String) : Response<String>{
        println(id)
        println(user.friends)
        println(user.rating)
        return withContext(Dispatchers.IO){
            val response = retrofit.create(UserApiClient::class.java).saveUser(user, id)
            println("he conseguido una respuesta")
            response
        }
    }
}