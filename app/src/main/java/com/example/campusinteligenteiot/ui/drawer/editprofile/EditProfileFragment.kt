package com.example.campusinteligenteiot.ui.drawer.editprofile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.EditProfileFragmentBinding

class EditProfileFragment : Fragment() {

    private  var _binding: EditProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EditProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }



}