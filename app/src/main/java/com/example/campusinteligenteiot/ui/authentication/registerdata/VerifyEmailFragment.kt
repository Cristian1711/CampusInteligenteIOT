package com.example.campusinteligenteiot.ui.authentication.registerdata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.databinding.FragmentVerifyEmailBinding
import com.example.campusinteligenteiot.usecases.AuthUserUseCase
import com.example.campusinteligenteiot.ui.authentication.signin.SigninVMFactory
import com.example.campusinteligenteiot.ui.authentication.signin.SigninViewModel

class VerifyEmailFragment : Fragment() {
    private lateinit var EMAIL: String
    private lateinit var PASSWD: String

    //ViewBiding
    private  var _binding: FragmentVerifyEmailBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val loginViewModel by viewModels<SigninViewModel> {
        SigninVMFactory(AuthUserUseCase(UserRepository()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val email = arguments?.getString("email").toString()
        val passwd = arguments?.getString("passwd").toString()

        EMAIL = email
        PASSWD = passwd

        println(EMAIL)
        println(PASSWD)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVerifyEmailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setOnClickListener {
            loginSetUp()
            viewModelLoginSetUp()
        }
    }


    private fun loginSetUp() {

        loginViewModel.login(EMAIL, PASSWD)
    }

    private fun viewModelLoginSetUp() {
        with(loginViewModel) {
            successLD.observe(viewLifecycleOwner) {
                activity?.also {
                    Toast.makeText(context,
                        R.string.email_verified,
                        Toast.LENGTH_SHORT).show()
                }
                goToRegisterData()
            }
            errorLD.observe(viewLifecycleOwner) { msg->
                activity?.also {
                    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun goToRegisterData() {
        //findNavController().navigate(R.id.action_verifyEmailFragment_to_registerUserFragment)
        //findNavController().navigate(R.id.action_verifyEmailFragment_to_phoneNumberFragment)
    }
}