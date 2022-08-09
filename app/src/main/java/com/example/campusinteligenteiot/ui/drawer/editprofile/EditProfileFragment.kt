package com.example.campusinteligenteiot.ui.drawer.editprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.EditProfileFragmentBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class EditProfileFragment : Fragment() {

    private  var _binding: EditProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EditProfileViewModel>()
    private lateinit var user: UsersResponse
    val IMAGE_CHOOSE = 1000
    private var imageUri: Uri?= null
    private lateinit var storage: FirebaseStorage
    private lateinit var url:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditProfileFragmentBinding.inflate(inflater,container,false)
        storage = Firebase.storage
        val args = arguments
        val gson = Gson()
        val json = arguments?.getString("current_user")
        user = gson.fromJson(json, UsersResponse::class.java)

        /*
        GlobalScope.launch(Dispatchers.Main) {
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

    private suspend fun observeUri() {
        viewModel.getImageFromStorage(user.profileImage).observe(viewLifecycleOwner, Observer {
            setImage(it)
        })
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
            findNavController().navigate(
                R.id.action_editProfileFragment_to_profileFragment
            )
        }

        binding.settingsButton.setOnClickListener{
            findNavController().navigate(
                R.id.action_editProfileFragment_to_configFragment)
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

        GlobalScope.launch(Dispatchers.Main) {
            val response = viewModel.saveUser(user, user.id)
            fileUpload()
            println("YA HE HECHO EL UPDATE SUPUESTAMENTE")
            println(response.code().toString())
            user.profileImage = url
            val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val gson = Gson()
            val json = gson.toJson(user)
            editor.putString("current_user", json)
            editor.commit()
        }

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

    private fun fileUpload() {

        val storageRef = storage.reference
        val name = Firebase.auth.currentUser?.uid.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy_hh:mm:ss")
        val currentDate = sdf.format(Date()).toString()
        val pathImage = storageRef.child("profiles/$name/$currentDate.png")

        // Get the data from an ImageView as bytes
        //binding.imageView1.isDrawingCacheEnabled = true
        //binding.imageView1.buildDrawingCache()
        val bitmap = (binding.itemImage.image2.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, baos)
        val data = baos.toByteArray()

        var uploadTask = pathImage.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(context, R.string.register_images_error, Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            Toast.makeText(context, R.string.register_images_success, Toast.LENGTH_SHORT).show()
        }


        val url = pathImage.downloadUrl.toString()
        uploadDataFirestore(currentDate)

    }

    private fun uploadDataFirestore(currentDate: String) {
        val userId = Firebase.auth.currentUser?.uid.toString()
        url = "gs://campusinteligenteiot.appspot.com/profiles/$userId/$currentDate.png"
        val db = Firebase.firestore
        db.collection("User")
            .document(userId)
            .update("profileImage", url)
            .addOnSuccessListener {
                Toast.makeText(context, R.string.firestore_upload_success, Toast.LENGTH_SHORT)
                    .show()

            }.addOnFailureListener {

                Toast.makeText(context, R.string.firestore_upload_failure, Toast.LENGTH_SHORT)
                    .show()

            }
    }


}