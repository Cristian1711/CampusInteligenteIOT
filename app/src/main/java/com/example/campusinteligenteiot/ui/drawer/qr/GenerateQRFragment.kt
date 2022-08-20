package com.example.campusinteligenteiot.ui.drawer.qr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.FragmentGenerateQRBinding
import com.example.campusinteligenteiot.databinding.ProfileFragmentBinding
import com.example.campusinteligenteiot.ui.drawer.profile.ProfileViewModel
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateQRFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateQRFragment : Fragment() {

    private  var _binding: FragmentGenerateQRBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by viewModels<GenerateQRViewModel>()
    private lateinit var user: UsersResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGenerateQRBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.GenerateQRButton.setOnClickListener {

        }

        binding.ReadQRButton.setOnClickListener {
            initScanner()
        }
    }

    private fun initScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt(getString(R.string.scan_qr))
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        println(result)

        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, getString(R.string.canceled), Toast.LENGTH_SHORT).show()
            } else {
                val idUser = result.contents.toString()
                GlobalScope.launch(Dispatchers.Main){
                    if(!user.friends.contains(idUser)){
                        user.friends.add(idUser)
                        viewModel.updateFriendList(user)
                    }
                    else{
                        Toast.makeText(context, getString(R.string.already_friend), Toast.LENGTH_SHORT).show()
                    }

                    val bundle = bundleOf(
                        "userId" to idUser
                    )
                    val navController = Navigation.findNavController(requireView())
                    navController.navigate(R.id.action_generateQRFragment_to_friendsProfileFragment2, bundle)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}