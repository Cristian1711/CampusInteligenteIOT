package com.example.campusinteligenteiot.ui.home.car

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.CarFragmentBinding
import com.example.campusinteligenteiot.ui.authentication.splash.CarViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CarFragment : Fragment() {
    private val viewModel by viewModels<CarViewModel>()
    private var _binding: CarFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUser: UsersResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = CarFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main){
            val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("current_user", "")
            currentUser = gson.fromJson(json, UsersResponse::class.java)
            viewModel.setUser(currentUser)
            viewModel.finishLD.observe(viewLifecycleOwner) { isDriver->
                if (isDriver) {
                    findNavController().navigate(R.id.action_navigation_car_to_carDriverFragment)

                } else {
                    findNavController().navigate(R.id.action_navigation_car_to_carPassengerFragment)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.resume()
    }

}