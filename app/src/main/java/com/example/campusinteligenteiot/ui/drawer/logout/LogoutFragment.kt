package com.example.campusinteligenteiot.ui.drawer.logout

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.LogoutFragmentBinding

class LogoutFragment : Fragment() {

    //ViewBiding
    private  var _binding: LogoutFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<LogoutViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LogoutFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setOnClickListener {
            gotoAuth(view)
        }
    }

    private fun gotoAuth(view: View) {
        viewModel.logout(view)

    }

}