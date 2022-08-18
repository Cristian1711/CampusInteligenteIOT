package com.example.campusinteligenteiot.ui.drawer.friends.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.FriendsProfileFragmentBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SellerProfileFragment : Fragment() {
    private var userId: String? = null
    private lateinit var user: UsersResponse
    private val viewModel by viewModels<FriendsProfileViewModel>()
    private lateinit var binding: FriendsProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getString("userId")
        GlobalScope.launch(Dispatchers.Main) {
            user = viewModel.getUserFromLocal(userId!!)!!
            setUserData(user)
            loadImage(user.profileImage)
        }

        binding = FriendsProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun loadImage(media: String) {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(media!!)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(binding.itemImage.image2)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemEditFriends.chatButton.setOnClickListener{
            findNavController().navigate(R.id.action_friendsProfileFragment2_to_channelsFragment)
        }

        binding.itemEditFriends.deleteButton.setOnClickListener{

        }

        binding.backButton.setOnClickListener{
            findNavController().navigate(R.id.action_sellerProfileFragment_to_navigation_shop)
        }

    }

}