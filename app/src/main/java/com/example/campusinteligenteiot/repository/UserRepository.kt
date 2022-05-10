package com.example.campusinteligenteiot.repository

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.annotation.IntegerRes
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.UserProvider
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.api.network.UserService
import com.example.campusinteligenteiot.common.Resource
import com.example.campusinteligenteiot.common.SingleLiveEvent
import com.example.campusinteligenteiot.model.Image
import com.example.campusinteligenteiot.model.User
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList


class UserRepository {

    private val bd = Firebase.firestore
    private val userTable = bd.collection("User")

    private val api = UserService()

     fun signUpDefault(email: String, passwd: String, repeatedPasswd: String) : Resource<FirebaseUser?>? {
        println("creando")

        /* Firebase.auth.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener {authResult->
             if (taskCompleted) return@addOnCompleteListener
             taskCompleted = true

             val newUser = Firebase.auth.currentUser
             if (authResult.isSuccessful && newUser != null) {
                 /*newUser.sendEmailVerification().addOnCompleteListener { emailTask->

                 }
                 Firebase.auth.signOut()*/
                 successSignUp.postValue(authResult.isSuccessful)
             }else {
                 println("No se ha podido registrar")
                 error.value = R.string.signup_error


             }
         }*/

        Firebase.auth.createUserWithEmailAndPassword(email,passwd)
        val user = Firebase.auth.currentUser
        return if (user != null) {
            println(user.email)
            Resource.Result(user)
        } else {
            null
        }

    }

     fun signInDefault(email: String, passwd: String) : Resource<FirebaseUser?>{

        println("inicio sesion")

        Firebase.auth.signInWithEmailAndPassword(email,passwd)
        val user = Firebase.auth.currentUser

        if (user != null) {
            println(user.email)
        }
        return Resource.Result(user)
    }

     fun getCurrentUser() : User{

        val uid = "7dscT5MXidZQjtL51MPQRi5ZdK62"
        val db = Firebase.firestore
        val userList = db.collection("User")

        val RefDoc = userList.document(uid).get().addOnSuccessListener { document ->
            if (document.exists()) {
                //user = documentToUser(document)

            }
        }

        val image = Image("1", "@drawable/img")
        val images = ArrayList<Image>()
        images.add(image)

        val user = User(
            "",
            "Paco",
            "Martinez",
            "pacoo23",
            Date(1,1, 2000),
            "paquito@gmail.com",
            "",
            null,
            "descripcion de prueba",
            "gs://racoonapps-cd246.appspot.com/profiles/placeholder.png",
            null,
            false,
            null
        )


        return user

    }

     fun documentToUser(document: DocumentSnapshot) : User {

         val birthdate = document.getDate("birthdate")
         val collegeDegree = document.getString("collegeDegree")
         val description = document.getString("description")
         val email = document.getString("email")
         val userName = document.getString("userName")
         val profileImage = document.getString("profileImage")
         val name = document.getString("name")
         val surname = document.getString("surname")


        val user = User(
            "",
            name,
            surname,
            userName,
            birthdate,
            email,
            collegeDegree,
            "677881122",
            description,
            profileImage,
        null,
        false,
        null)

        return user
    }

    suspend fun getSingleUser(uid: String): User {

        var user = User()
        val job1 = userTable.document(uid)
            .get()
            .addOnSuccessListener { document ->
                if(document.exists()){
                    println("EL DOCUMENTO EXISTE")
                    user = documentToUser(document)
                }
            }
        job1.await()
        return user

    }

    suspend fun searchUserById(id: String):UsersResponse{
        val response = api.searchUserById(id)
        return response
    }

    suspend fun getAllUsers():List<UsersResponse>{
        val response = api.getAllUsers()
        UserProvider.users = response
        return response
    }

    suspend fun saveUser(id: String, user: UsersResponse){
        api.saveUser(id, user)
    }

    fun getImageFromStorage(media: String?): Uri? {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(media!!)
        return gsReference.downloadUrl.result
    }

    fun uploadFile(drawable: Drawable) {

    }


}