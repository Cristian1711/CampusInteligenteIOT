package com.example.campusinteligenteiot.ui.drawer.editprofile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.databinding.EditProfileFragmentBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson

class EditProfileFragment : Fragment() {

    private  var _binding: EditProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EditProfileViewModel>()
    private lateinit var user: UsersResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditProfileFragmentBinding.inflate(inflater,container,false)

        val args = arguments
        val gson = Gson()
        val json = arguments?.getString("current_user")
        user = gson.fromJson(json, UsersResponse::class.java)

        loadImage(user.profileImage)
        setUserData(user)



        return binding.root
    }

    private fun setUserData(user: UsersResponse?) {
        binding.itemImage.usernameEdit.setText(user!!.userName)
        binding.itemImage.descriptionEdit.setText(user!!.description)

        binding.itemEdit.nameEdit.setText(user.name + " " + user.surname)

        binding.itemCollegedegree.collegeDegree.text = user.collegeDegree

        binding.itemEmail.email.text = user.email
    }

    private fun loadImage(media: String) {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(media!!)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(binding.itemImage.image2)
        }
    }


}