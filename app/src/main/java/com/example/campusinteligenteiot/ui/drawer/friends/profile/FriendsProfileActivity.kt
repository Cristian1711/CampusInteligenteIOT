package com.example.campusinteligenteiot.ui.drawer.friends.profile

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.databinding.ActivityFriendsProfileBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FriendsProfileActivity : AppCompatActivity() {

    private val viewModel by viewModels<FriendsProfileViewModel>()
    private lateinit var binding: ActivityFriendsProfileBinding
    private lateinit var userId: String
    private lateinit var user: UsersResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        userId = bundle?.getString("userId").toString()

        GlobalScope.launch(Dispatchers.Main) {
            user = viewModel.getUserFromLocal(userId)!!
            setUserData(user)
            loadImage(user.profileImage)
        }

        binding = ActivityFriendsProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun loadImage(media: String) {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(media!!)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(binding.itemImage.image2)
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

        binding.itemEditFriends.chatButton.setOnClickListener{

        }

        binding.itemEditFriends.deleteButton.setOnClickListener{

        }
    }



    private fun setUserData(user: UsersResponse) {

        binding.profileName.text = user.userName

        binding.itemImage.username.text = user.userName

        binding.itemImage.description.text = user.description

        binding.itemEditFriends.name.text = (user.name + " " + user.surname)

        binding.itemCollegedegree.collegeDegree.text = user.collegeDegree

        binding.itemEmail.email.text = user.email

    }
}