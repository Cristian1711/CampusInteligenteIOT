package com.example.campusinteligenteiot.model

import java.util.Date

class User (val idUser: String? = null,
            val name: String? = null,
            val surname: String? = null,
            val userName: String? = null,
            val birthdate: Date? = null,
            val email: String? = null,
            val collegeDegree: String? = null,
            val phoneNumber: String? = null,
            val description: String? = null,
            val profileImage: String? = null,
            val friends: ArrayList<String>? = null,
            val isDriver: Boolean = false
        )
{
    fun getAge(): String{
        return "19"
    }

}