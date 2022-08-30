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


class RegisterBirthdateFragment : Fragment() {

    private val viewModel by viewModels<RegisterBirthdateViewModel>()
    private var friendId = "2dhCruKw3IJAKR5jrCxH"
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
    private var PRODUCTLIKES: ArrayList<String>? = null
    private var APPOINTMENTSTITLES: ArrayList<String>? = null
    private var APPOINTMENTSDATES: ArrayList<String>? = null
    private var APPOINTMENTSHOURS: ArrayList<String>? = null
    private var DESCRIPTION = "nada"
    private var ISDRIVER = false
    private var PROFILEIMAGE = "nada"
    private var RATING = hashMapOf(
        "rating" to 0
    )
    //private var PHONENUMBER = "phonenumber"

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
        //val phoneNumber = arguments?.getString("phoneNumber")

        //PHONENUMBER = phoneNumber!!
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
        val datePicker = DatePickerFragment {
                day, month, year -> onDateSelected(day, month, year)
        }
        val context =
            datePicker.show(parentFragmentManager,"datePicker")
    }

    private fun onDateSelected (day:Int, month:Int, year:Int) {
        val selectedMonth = month + 1
        binding.etDate.setText("$day / $selectedMonth / $year")

        DAY = day
        MONTH = selectedMonth
        YEAR = year

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
            "profileImage" to PROFILEIMAGE,
            "rating" to RATING,
            "productLikes" to PRODUCTLIKES,
            "appointmentsTitles" to APPOINTMENTSTITLES,
            "appointmentsHours" to APPOINTMENTSHOURS,
            "appointmentsDates" to APPOINTMENTSDATES
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