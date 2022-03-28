package com.example.campusinteligenteiot.ui.authentication.registerdata

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.FragmentRegisterDescriptionBinding


class RegisterDescriptionFragment : Fragment() {

    private val viewModel by viewModels<RegisterDescriptionViewModel>()

    //ViewBiding
    private  var _binding: FragmentRegisterDescriptionBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterDescriptionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setOnClickListener {

            val description = binding.editTextTextMultiLine.text.toString()

            uploadDataFirebase(description)

            viewModel.gotoHome(view)
        }


    }

    private fun uploadDataFirebase(description: String) {
        val userId = Firebase.auth.currentUser?.uid.toString()

        val db = Firebase.firestore
        db.collection("User")
            .document(userId)
            .update("description", description)
            .addOnSuccessListener {


            }.addOnFailureListener {

                Toast.makeText(context, R.string.firestore_upload_error, Toast.LENGTH_SHORT)
                    .show()

            }
    }


}