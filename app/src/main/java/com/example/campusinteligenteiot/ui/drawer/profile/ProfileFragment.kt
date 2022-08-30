package com.example.campusinteligenteiot.ui.drawer.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ProfileFragmentBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private  var _binding: ProfileFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var user: UsersResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)
        /*
        GlobalScope.launch(Dispatchers.Main) {
            println(user.profileImage)
            observeUri()
        }
         */

        loadImage(user.profileImage)

        setUserData(user)

        return binding.root
    }

    private fun loadImage(media: String){
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(media!!)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it).into(binding.itemImage.image2)
        }
    }

    fun toString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("dd/MM/yyyy").format(this)
    }

    private suspend fun observeUri() {
        viewModel.getImageFromStorage(user.profileImage).observe(viewLifecycleOwner, Observer {
            setImage(it)
        })
    }

    private fun setImage(uri: Uri) {
        Glide.with(this).load(uri).into(binding.itemImage.image2)
    }

    private fun setUserData(user: UsersResponse) {
        binding.itemImage.username.text = user.userName

        binding.itemImage.description.text = user.description

        binding.itemEdit.name.text = (user.name + " " + user.surname)

        binding.itemCollegedegree.collegeDegree.text = user.collegeDegree

        binding.itemEmail.email.text = user.email

        binding.itemBirthdate.birthdate.text = user.birthdate
    }


        

 

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsButton.setOnClickListener{
            findNavController().navigate(
                R.id.action_profileFragment_to_configFragment)
        }

        binding.itemEdit.editButton.setOnClickListener{
            goToEdit()
        }
    }

    private fun goToEdit() {
        val args = Bundle()
        val gson = Gson()
        val json = gson.toJson(user)
        args.putString("current_user", json)

        findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment, args)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}