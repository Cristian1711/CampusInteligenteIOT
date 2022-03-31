package com.example.campusinteligenteiot.ui.authentication.signin

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.databinding.FragmentSigninBinding
import com.example.campusinteligenteiot.usecases.AuthUserUseCase
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.QuerySnapshot




class SigninFragment : Fragment() {

    private val GOOGLE_SIGN_IN = 10
    private val YEAR = 1
    private val MONTH = 1
    private val DAY = 1
    private val TAGS = arrayListOf<String>()

    //private val viewModel by viewModels<LoginViewModel>()
    private val viewModel by viewModels<SigninViewModel> {
        SigninVMFactory(AuthUserUseCase(UserRepository()))
    }

    //ViewBiding
    private var _binding: FragmentSigninBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            openSignUp()
        }
        binding.signInButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            setUp()
            viewModelSetup(view)
            binding.progressBar.visibility = View.GONE
        }

        binding.googleButton.setOnClickListener {
            setUpGoogle()
        }
    }

    private fun setUpGoogle() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_token))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(requireActivity(),googleConf)
        googleClient.signOut()

        startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                println("try")
                val account = task.getResult(ApiException::class.java)

                if (account != null) {

                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                        if (it.isSuccessful) {
                            firstTimeGoogleSignUp()

                        }else {
                            Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }catch (e: ApiException) {
                println(e)
                Toast.makeText(context, R.string.google_login_error, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun firstTimeGoogleSignUp() {
        val userId = Firebase.auth.currentUser?.uid.toString()
        val db = Firebase.firestore
        val docRef = db.collection("User").document(userId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    //findNavController().navigate(R.id.action_loginFragment_to_registerUserFragment2)
                }else{
                    viewModel.gotoHome(requireView())
                }
            }
            .addOnFailureListener {
            }
    }


    private fun setUp() {

        val email = binding.emailEditText2.text.toString()
        val passwd = binding.passwordEditText2.text.toString()

        viewModel.login(email, passwd)

    }

    private fun viewModelSetup(view: View) {
        with(viewModel) {
            signUpLD.observe(viewLifecycleOwner) {
                openSignUp()
            }
            successLD.observe(viewLifecycleOwner) {
                activity?.also {
                }
                openHome(view)
            }
            errorLD.observe(viewLifecycleOwner) { msg ->
                activity?.also {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openHome(view: View) {
        viewModel.gotoHome(view)
    }

    private fun openSignUp() {
        findNavController().navigate(R.id.action_signinFragment_to_signupFragment)

    }
}