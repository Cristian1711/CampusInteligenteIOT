package com.example.campusinteligenteiot.ui.drawer.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {

    private  var _binding: EditProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EditProfileViewModel>()
    private lateinit var user: UsersResponse
    val IMAGE_CHOOSE = 1000
    private var imageUri: Uri?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditProfileFragmentBinding.inflate(inflater,container,false)

        val args = arguments
        val gson = Gson()
        val json = arguments?.getString("current_user")
        user = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main) {
            val uri = viewModel.getImageFromStorage(user.profileImage)
            setImage(uri!!)
        }

        setUserData(user)

        return binding.root
    }

    private fun setImage(uri: Uri) {
        Glide.with(this).load(uri).into(binding.itemImage.image2)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemImage.galleryButton.setOnClickListener{
            chooseProfileImage()
        }

        binding.saveDataButton.setOnClickListener{
            editUser(user)
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.saveUser(user.id, user)
            }
        }
    }

    private fun chooseProfileImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    private fun editUser(user: UsersResponse) {

        user.userName = binding.itemImage.usernameEdit.text.toString()
        user.description = binding.itemImage.descriptionEdit.text.toString()
        user.name = binding.itemEdit.nameEdit.text.toString()
    }

    private fun setUserData(user: UsersResponse?) {
        binding.itemImage.usernameEdit.setText(user!!.userName)
        binding.itemImage.descriptionEdit.setText(user!!.description)

        binding.itemEdit.nameEdit.setText(user.name + " " + user.surname)

        binding.itemCollegedegree.collegeDegree.text = user.collegeDegree

        binding.itemEmail.email.text = user.email
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK){
            imageUri = data?.data
            binding.itemImage.image2.setImageURI(imageUri)
        }
    }


}