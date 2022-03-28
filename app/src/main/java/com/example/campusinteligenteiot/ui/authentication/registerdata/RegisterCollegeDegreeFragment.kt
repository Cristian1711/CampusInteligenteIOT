package com.example.campusinteligenteiot.ui.authentication.registerdata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.FragmentRegisterCollegeDegreeBinding


class RegisterCollegeDegreeFragment : Fragment(), AdapterView.OnItemClickListener {

    private var NAME = "name"
    private var SURNAME = "surname"
    private var USERNAME = "username"
    private var GENDER = "gender"
    //private var PHONENUMBER = "phonenumber"

    //ViewBiding
    private  var _binding: FragmentRegisterCollegeDegreeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = arguments?.getString("name")
        val surname = arguments?.getString("surname")
        val username = arguments?.getString("username")
        //val phoneNumber = arguments?.getString("phoneNumber")

        //PHONENUMBER = phoneNumber!!
        NAME = name!!
        SURNAME = surname!!
        USERNAME = username!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterCollegeDegreeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUp()

        binding.nextButton.setOnClickListener {
            if (GENDER != "gender") {
                goNext()
            }else {
                Toast.makeText(context,R.string.register_college_degree_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setUp() {
        val genders = resources.getStringArray(R.array.collegeDegrees)
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.list_item,
                genders
            )
        }

        with(binding.genderTypeTextView) {
            setAdapter(adapter)
            onItemClickListener = this@RegisterCollegeDegreeFragment
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position).toString()
        GENDER = item

    }

    private fun goNext() {

        val bundle = bundleOf(
            //"phoneNumber" to PHONENUMBER,
            "name" to NAME,
            "surname" to SURNAME,
            "username" to USERNAME,
            "gender" to GENDER
        )

        //findNavController().navigate(R.id.action_registerUserGenderFragment_to_registerUserBirthdateFragment,bundle)
    }
}