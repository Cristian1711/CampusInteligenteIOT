package com.example.campusinteligenteiot.ui.drawer.logout

import android.view.View
import android.content.Intent
import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.campusinteligenteiot.ui.authentication.AuthenticationActivity

class LogoutViewModel : ViewModel() {

    fun logout(view: View) {
        Firebase.auth.signOut()
        val context: Context = view.context
        val intent = Intent(context, AuthenticationActivity::class.java)
        context.startActivity(intent)
    }

}