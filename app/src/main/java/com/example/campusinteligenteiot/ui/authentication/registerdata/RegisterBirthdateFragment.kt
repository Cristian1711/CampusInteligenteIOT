package com.example.campusinteligenteiot.ui.authentication.registerdata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.FragmentRegisterBirthdateBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import android.widget.DatePicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener


class RegisterBirthdateFragment : Fragment() {

    private val viewModel by viewModels<RegisterBirthdateViewModel>()
    private var friendId = "2dhCruKw3IJAKR5jrCxH" //soporte
    private var NAME = "name"
    private var SURNAME = "surname"
    private var USERNAME = "username"
    private var COLLEGEDEGREE = "collegeDegree"
    private var DAY = 0
    private var MONTH = 0
    private var YEAR = 0
    private var FRIENDS = arrayListOf(
        friendId
    )
    private var DESCRIPTION = "nada"
    private var ISDRIVER = false
    private var PROFILEIMAGE = "nada"

    //ViewBiding
    private  var _binding: FragmentRegisterBirthdateBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = arguments?.getString("name")
        val surname = arguments?.getString("surname")
        val username = arguments?.getString("username")
        val collegeDegree = arguments?.getString("collegeDegree")

        NAME = name!!
        SURNAME = surname!!
        USERNAME = username!!
        COLLEGEDEGREE = collegeDegree!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBirthdateBinding.inflate(inflater,container,false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.nextButton.setOnClickListener {
            if (DAY != 0 && MONTH != 0 && YEAR != 0) {
                uploadDataFirestore()
                goNext()
            }else {
                Toast.makeText(context,R.string.register_birthdate_error,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendario = Calendar.getInstance()
        val yy = calendario[Calendar.YEAR]
        val mm = calendario[Calendar.MONTH]
        val dd = calendario[Calendar.DAY_OF_MONTH]


        val datePicker = DatePickerDialog(
            requireActivity(),
            { view, year, monthOfYear, dayOfMonth ->
                val fecha = "$dayOfMonth / $monthOfYear / $year"
                DAY = dayOfMonth
                MONTH = monthOfYear
                YEAR = year
                binding.etDate.setText(fecha)
            }, yy, mm, dd
        )

        datePicker.show()
    }

    private fun onDateSelected (day:Int, month:Int, year:Int) {
        val selectedMonth = month + 1
        binding.etDate.setText("$day / $selectedMonth / $year")

    }

    private fun goNext() {
        val bundle = bundleOf(
            //"phoneNumber" to PHONENUMBER,
            "name" to NAME,
            "surname" to SURNAME,
            "username" to USERNAME,
            "collegeDegree" to COLLEGEDEGREE,
            "day" to DAY,
            "month" to MONTH,
            "year" to YEAR
        )
        findNavController().navigate(
            R.id.action_registerBirthdateFragment_to_registerImageFragment,
            bundle)

    }

    private fun uploadDataFirestore() {
        val email = Firebase.auth.currentUser?.email.toString()
        val userId = Firebase.auth.currentUser?.uid.toString()


        val data = hashMapOf(
            "name" to NAME,
            "surname" to SURNAME,
            "collegeDegree" to COLLEGEDEGREE,
            "birthdate" to "$DAY/$MONTH/$YEAR",
            "email" to email,
            "userName" to USERNAME,
            "description" to DESCRIPTION,
            "friends" to FRIENDS,
            "isDriver" to ISDRIVER,
            "profileImage" to PROFILEIMAGE
        )
        val db = Firebase.firestore
        db.collection("User")
            .document(userId)
            .set(data)
            .addOnSuccessListener {

            }.addOnFailureListener {

                Toast.makeText(context, R.string.firestore_upload_error, Toast.LENGTH_SHORT)
                    .show()

            }

    }


}