package com.example.campusinteligenteiot.ui.authentication.signup

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
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.databinding.FragmentSignupBinding
import com.example.campusinteligenteiot.usecases.user.AuthUserUseCase

class SignUpFragment : Fragment() {

    private lateinit var EMAIL: String
    private lateinit var PASSWD: String
    private val GOOGLE_SIGN_IN = 10


    //private val viewModel by viewModels<SignUpViewModel>()
    private val  viewModel by viewModels<SignUpViewModel> {
        SignUpVMFactory(AuthUserUseCase(UserRepository()))
    }


    //ViewBiding
    private  var _binding: FragmentSignupBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            setUp()
            setUpViewModel()
            binding.progressBar.visibility = View.GONE

        }

    }



    private fun setUp() {
        val email = binding.emailEditText.text.toString()
        val passwd = binding.passwordEditText.text.toString()
        val passwdRepeat = binding.repeatPasswordEditText.text.toString()

        viewModel.create(email, passwd,passwdRepeat)

        EMAIL = email
        PASSWD = passwd

    }

    private fun setUpViewModel() {
        with(viewModel) {
            successLD.observe(viewLifecycleOwner) {

                openEmailVerify()

                activity?.also {
                }

            }
            errorLD.observe(viewLifecycleOwner) {
                activity?.also {
                    Toast.makeText(context,R.string.signup_error,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openEmailVerify() {
        val bundle = bundleOf(
            "email" to EMAIL,
            "passwd" to PASSWD,

            )
        findNavController().navigate(R.id.action_signupFragment_to_verifyEmailFragment,bundle)
    }
}