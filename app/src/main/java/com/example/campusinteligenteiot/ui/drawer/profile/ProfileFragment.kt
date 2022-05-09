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
import com.example.campusinteligenteiot.databinding.ProfileFragmentBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.item_collegedegree.view.*
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.nav_header_drawer.view.*
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    private  var _binding: ProfileFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)

        val media = sharedPreferences?.getString("user_profileimage", "null")

        loadImage(media)

        binding.itemImage.username.text = sharedPreferences?.getString("user_username", "null")

        binding.itemImage.description.text = sharedPreferences?.getString("user_description", "null")

        binding.itemEdit.name.text = (sharedPreferences?.getString("user_name", "null")
                + " " + sharedPreferences?.getString("user_surname", "null"))

        binding.itemCollegedegree.collegeDegree.text = sharedPreferences?.getString("user_collegeDegree", "null")

        binding.itemEmail.email.text = sharedPreferences?.getString("user_email", "null")

        return binding.root
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}