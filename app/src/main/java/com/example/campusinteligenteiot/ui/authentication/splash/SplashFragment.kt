package com.example.campusinteligenteiot.ui.authentication.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private val viewModel by viewModels<SplashViewModel>()
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.finishLD.observe(viewLifecycleOwner) { isUserLogged->
            if (isUserLogged) {
                openHome(view)

            } else {
                findNavController().navigate(R.id.action_splashFragment_to_signinFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.resume()
    }

    private fun openHome(view: View) {
        viewModel.gotoHome(view)
    }

}