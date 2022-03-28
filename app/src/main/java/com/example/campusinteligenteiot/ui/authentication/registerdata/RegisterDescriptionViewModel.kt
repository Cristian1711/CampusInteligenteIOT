package com.example.campusinteligenteiot.ui.authentication.registerdata

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.ui.home.HomeActivity

class RegisterDescriptionViewModel : ViewModel() {

    fun gotoHome(view: View) {
        val context: Context = view.context
        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)
    }
}