package com.example.campusinteligenteiot.ui.home

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.UserProvider
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.databinding.ActivityHomeBinding
import com.example.campusinteligenteiot.ui.home.car.CarFragment
import com.example.campusinteligenteiot.ui.home.main.MainHomeViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_drawer.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.security.AccessController.getContext

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var user: UsersResponse
    private val viewModel by viewModels<MainHomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navViewDrawer: NavigationView = findViewById(R.id.navigation_view)

        //val navController = findNavController(R.id.fragmentContainerView2)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_car,R.id.navigation_shop,R.id.navigation_map, R.id.navigation_events,R.id.navigation_schedule)
        )

        navView.setupWithNavController(navController)
        navViewDrawer.setupWithNavController(navController)

        viewModel.onCreate()
        viewModel.resultUsers.observe(this, Observer {

            UserProvider.users = viewModel.resultUsers.value!!
            println("Lista de usuarios")
            println(UserProvider.users)
            GlobalScope.launch(Dispatchers.Main) {
                user = viewModel.getUserFromLocal(viewModel.getId())
                println("DATOS USUARIO DE BASE DE DATOS LOCAL")
                println(user.name)
                println(user.collegeDegree)
                println(user.surname)
                val media = user.profileImage
                val storageReference = FirebaseStorage.getInstance()
                val gsReference = storageReference.getReferenceFromUrl(media!!)
                gsReference.downloadUrl.addOnSuccessListener {
                    Glide.with(this@HomeActivity).load(it).into(binding.navigationView.getHeaderView(0).imageView)
                    binding.navigationView.getHeaderView(0).textView2.setText(user.name)
                }

                val sharedPreferences = baseContext.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val gson = Gson()
                val json = gson.toJson(user)
                editor.putString("current_user", json)
                editor.commit()
            }


        })

        binding.DrawerButton.setOnClickListener {
            val sharedPreferences = baseContext.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("current_user", "")
            user = gson.fromJson(json, UsersResponse::class.java)
            val media = user.profileImage
            val storageReference = FirebaseStorage.getInstance()
            val gsReference = storageReference.getReferenceFromUrl(media!!)
            gsReference.downloadUrl.addOnSuccessListener {
                Glide.with(this@HomeActivity).load(it).into(binding.navigationView.getHeaderView(0).imageView)
                binding.navigationView.getHeaderView(0).textView2.setText(user.name)
            }
            drawerLayout.openDrawer(GravityCompat.START)
        }





        //setupActionBarWithNavController(navController, appBarConfiguration)


    }





}