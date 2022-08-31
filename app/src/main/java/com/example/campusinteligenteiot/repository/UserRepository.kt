package com.example.campusinteligenteiot.repository

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.campusinteligenteiot.api.model.user.UserProvider
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.api.network.user.UserService
import com.example.campusinteligenteiot.common.Resource
import com.example.campusinteligenteiot.model.Image
import com.example.campusinteligenteiot.model.User
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class UserRepository {

    private val bd = Firebase.firestore
    private val userTable = bd.collection("User")
    val uri: MutableLiveData<Uri>? = null

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

    suspend fun searchUserById(id: String): UsersResponse {
        val response = api.searchUserById(id)
        return response
    }

    suspend fun saveProductLikes(idProducts: ArrayList<String>, id: String){
        api.saveProductLikes(idProducts, id)
    }

    suspend fun getAllUsers():List<UsersResponse>{
        val response = api.getAllUsers()
        UserProvider.users = response
        return response
    }

    suspend fun saveUser(user: UsersResponse, id: String) : Response<String> {
       return api.saveUser(user, id)
    }

    fun getImageFromStorage(media: String?): MutableLiveData<Uri> {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(media!!)
        println(media)
        println(gsReference)
        gsReference.downloadUrl.addOnSuccessListener {
             println("TODO BIEN")
            uri?.value = it
         }
        println(uri)
        return uri!!
    }

    fun uploadFile(drawable: Drawable) {

    }





}