package com.example.campusinteligenteiot.ui.drawer.friends.profile

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.FriendsProfileFragmentBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import io.getstream.chat.android.client.ChatClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FriendsProfileFragment : Fragment() {
    private var userId: String? = null
    private var type: String? = null
    private var referId: String? = null
    private lateinit var user: UsersResponse
    private lateinit var currentUser: UsersResponse
    private val viewModel by viewModels<FriendsProfileViewModel>()
    private lateinit var binding: FriendsProfileFragmentBinding
    private val client = ChatClient.instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getString("userId")
        type = arguments?.getString("type")
        referId = arguments?.getString("referId")
        GlobalScope.launch(Dispatchers.Main) {
            val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("current_user", "")
            currentUser = gson.fromJson(json, UsersResponse::class.java)
            user = viewModel.getUserFromLocal(userId!!)!!
            setUserData(user)
            loadImage(user.profileImage)


            if(user.friends.contains(currentUser.id) && !currentUser.friends.contains(user.id)){
                showAlertDialog()
            }

            if(user.friends.contains(currentUser.id) && currentUser.friends.contains(user.id)){
                binding.itemEditFriends.deleteButton.visibility = VISIBLE
            }
            else{
                binding.itemEditFriends.deleteButton.visibility = INVISIBLE
            }
        }

        binding = FriendsProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun loadImage(media: String) {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(media)
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

        binding.itemBirthdate.birthdate.text = user.birthdate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemEditFriends.chatButton.setOnClickListener{
            if(user.friends.contains(currentUser.id) && currentUser.friends.contains(user.id)){
                findNavController().navigate(R.id.action_friendsProfileFragment2_to_channelsFragment)
            }
            else{
                Toast.makeText(requireContext(), getString(R.string.warning_chat), Toast.LENGTH_SHORT).show()
            }

        }

        binding.itemEditFriends.deleteButton.setOnClickListener{
            showAlertDialogDelete()
        }

        binding.backButton.setOnClickListener {
            if(type == null){
                findNavController().navigate(R.id.action_friendsProfileFragment2_to_friendsFragment)
            }
            else{
                if(type.equals("evento")){
                    val bundle = bundleOf(
                        "eventId" to referId
                    )
                    findNavController().navigate(R.id.action_friendsProfileFragment2_to_eventInformationFragment, bundle)
                }
                else{
                    val bundle = bundleOf(
                        "tripId" to referId
                    )
                    findNavController().navigate(R.id.action_friendsProfileFragment2_to_tripDetailsFragment, bundle)
                }
            }
        }

    }

    private fun showAlertDialogDelete() {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle(getString(R.string.are_u_sure))
            setMessage(getString(R.string.first_part_delete_friend) + user.userName + getString(R.string.second_part_delete_friend))
            setPositiveButton("Sí") { dialog: DialogInterface, _: Int ->
                currentUser.friends.remove(user.id)
                GlobalScope.launch(Dispatchers.Main){
                    viewModel.saveFriends(currentUser.friends, currentUser.id)
                    Toast.makeText(requireContext(), getString(R.string.delete_friend_success), Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
            }
            setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle(getString(R.string.friend_request))
            setMessage(getString(R.string.user) + user.userName + getString(R.string.second_part_question))
            setPositiveButton("Sí") { dialog: DialogInterface, _: Int ->
                currentUser.friends.add(user.id)
                GlobalScope.launch(Dispatchers.Main){
                    viewModel.saveFriends(currentUser.friends, currentUser.id)
                    createChat(currentUser.userName, user.userName)
                    Toast.makeText(requireContext(), getString(R.string.first_part_new_friend) + user.userName + getString(
                        R.string.second_part_new_friend), Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
            }
            setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
        }.create().show()
    }

    private fun createChat(currentUserUserName: String, sellerUserName: String) {
        client.createChannel(
            channelType = "messaging",
            members = listOf(currentUserUserName,sellerUserName)
        ).enqueue { result ->
            if (result.isSuccess) {
                val channel = result.data()
            }
        }
    }

}