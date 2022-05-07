package com.example.campusinteligenteiot.api.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class UsersResponse(
    @SerializedName("id") var id: String,
    @SerializedName("birthdate") var birthdate: Date,
    @SerializedName("collegeDegree") var collegeDegree: String,
    @SerializedName("description") var description: String,
    @SerializedName("email") var email: String,
    @SerializedName("friends") var friends: ArrayList<String>,
    @SerializedName("name") var name: String,
    @SerializedName("profileImage") var profileImage: String,
    @SerializedName("rating") var rating: HashMap<String, Integer>,
    @SerializedName("surname") var surname: String,
    @SerializedName("userName") var userName: String,
    @SerializedName("driver") var driver: Boolean
)