package com.example.campusinteligenteiot.ui.authentication.signin

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.repository.UserRepository
import com.example.campusinteligenteiot.databinding.FragmentSigninBinding
import com.example.campusinteligenteiot.usecases.user.AuthUserUseCase
import kotlinx.android.synthetic.main.generic_dialog_1_button.view.*
import kotlinx.android.synthetic.main.generic_dialog_1_button.view.cancelButton
import kotlinx.android.synthetic.main.report_dialog.view.*


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

        binding.textView3.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            val myView = LayoutInflater.from(context).inflate(R.layout.report_dialog, null)
            builder.setView(myView)
            val dialog = builder.create()
            dialog.show()

            myView.cancelButton.setOnClickListener{
                dialog.cancel()
            }

            myView.button.setOnClickListener {
                sendEmail(myView.emailContent.text.toString())
                dialog.cancel()
            }
        }
    }

    private fun sendEmail(text: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:cristiaangonzaleez36@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Error de inicio de sesión")
            putExtra(Intent.EXTRA_TEXT, text)
        }

        startActivity(emailIntent)
    }

    private fun setUpGoogle() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
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

            try{
                val account = task.getResult(ApiException::class.java)

                if(account != null){

                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            firstTimeGoogleSignUp()
                        }
                        else{
                            Toast.makeText(context, R.string.login_error, Toast.LENGTH_SHORT).show()
                        }
                    }


                }
            }catch (e: ApiException){
                Toast.makeText(context, getString(R.string.error_google), Toast.LENGTH_SHORT).show()
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
                    findNavController().navigate(R.id.action_signinFragment_to_registerNameFragment)
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