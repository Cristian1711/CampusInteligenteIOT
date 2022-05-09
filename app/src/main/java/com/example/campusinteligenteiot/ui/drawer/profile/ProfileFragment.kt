package com.example.campusinteligenteiot.ui.drawer.profile

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.databinding.ProfileFragmentBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_collegedegree.view.*
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.nav_header_drawer.view.*
import kotlinx.android.synthetic.main.profile_fragment.*

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

        loadImage(user.profileImage)
        setUserData(user)

        return binding.root
    }

    private fun setUserData(user: UsersResponse) {
        binding.itemImage.username.text = user.userName

        binding.itemImage.description.text = user.description

        binding.itemEdit.name.text = (user.name + " " + user.surname)

        binding.itemCollegedegree.collegeDegree.text = user.collegeDegree

        binding.itemEmail.email.text = user.email
    }

    private fun loadImage(media: String?) {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(media!!)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(binding.itemImage.image2)
        }
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}