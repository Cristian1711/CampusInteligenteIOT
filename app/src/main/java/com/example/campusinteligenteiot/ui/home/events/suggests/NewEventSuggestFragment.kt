package com.example.campusinteligenteiot.ui.home.events.suggests

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.AddNewProductFragmentBinding
import com.example.campusinteligenteiot.databinding.NewEventSuggestFragmentBinding
import com.example.campusinteligenteiot.ui.authentication.registerdata.DatePickerFragment
import com.example.campusinteligenteiot.ui.home.shop.myProducts.addProduct.AddNewProductViewModel
import com.google.firebase.Timestamp
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

class NewEventSuggestFragment : Fragment() {
    private  var _binding: NewEventSuggestFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NewEventSuggestViewModel>()
    private var YEAR = 0
    private var DAY = 0
    private var MONTH = 0
    private lateinit var monthString:String
    private lateinit var dayString:String
    private lateinit var yearString:String
    private lateinit var event: EventCall
    private val IMAGE_CHOOSE = 1000
    private var imageUri: Uri? = null
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NewEventSuggestFragmentBinding.inflate(inflater,container,false)
        storage = Firebase.storage

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener{
            findNavController().navigate(R.id.action_newEventSuggestFragment_to_navigation_events)
        }

        binding.datePicker.setOnClickListener{
            showDatePickerDialog()
        }

        binding.buttonGallery.setOnClickListener {
            chooseImageGallery()
        }

        binding.buttonSave.setOnClickListener {
            if (DAY != 0 && MONTH != 0 && YEAR != 0) {
                val eventDate = Calendar.getInstance()

                eventDate.clear()
                eventDate.set(Calendar.YEAR,YEAR)
                eventDate.set(Calendar.MONTH,MONTH)
                eventDate.set(Calendar.DAY_OF_MONTH,DAY)

                event = EventCall(
                    null,
                    null,
                    "$yearString-$monthString-$dayString",
                    binding.textDescription.text.toString(),
                    null,
                    binding.textTitleEvent.text.toString(),
                    binding.textEventPlace.text.toString(),
                    true
                )

                GlobalScope.launch(Dispatchers.Main) {
                    val response = viewModel.saveSuggestion("null", event)
                    fileUpload(response.body()!!)
                }
            }else {
                Toast.makeText(context,R.string.register_birthdate_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    private fun fileUpload(id: String) {
        val storageRef = storage.reference
        val name = Firebase.auth.currentUser?.uid.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy_hh:mm:ss")
        val currentDate = sdf.format(Date()).toString()
        val pathImage = storageRef.child("profiles/$name/$currentDate.png")

        // Get the data from an ImageView as bytes
        //binding.imageView1.isDrawingCacheEnabled = true
        //binding.imageView1.buildDrawingCache()
        val bitmap = (binding.eventImage.drawable as BitmapDrawable).bitmap
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
        uploadDataFirestore(currentDate, id)
    }

    private fun uploadDataFirestore(currentDate: String, id: String) {
        val userId = Firebase.auth.currentUser?.uid.toString()
        val url = "gs://campusinteligenteiot.appspot.com/profiles/$userId/$currentDate.png"
        event.eventImage = url
        val db = Firebase.firestore
        db.collection("Event")
            .document(id)
            .update("eventImage", url)
            .addOnSuccessListener {
                Toast.makeText(context, R.string.firestore_upload_success, Toast.LENGTH_SHORT)
                    .show()
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_newEventSuggestFragment_to_navigation_events)
                }, 3000)

            }.addOnFailureListener {

                Toast.makeText(context, R.string.firestore_upload_failure, Toast.LENGTH_SHORT)
                    .show()

            }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment {
                day, month, year -> onDateSelected(day, month, year)
        }
        val context =
            datePicker.show(parentFragmentManager,"datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val selectedMonth = month + 1
        binding.datePicker.setText("$day / $selectedMonth / $year")

        DAY = day
        MONTH = month
        YEAR = year

        monthString = (month+1).toString()
        if (monthString.length == 1) {
            monthString = "0" + monthString
        }
        dayString = day.toString()
        if(dayString.length == 1){
            dayString = "0" + dayString
        }

        yearString = year.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            binding.eventImage.setImageURI(imageUri)
        }
    }

}