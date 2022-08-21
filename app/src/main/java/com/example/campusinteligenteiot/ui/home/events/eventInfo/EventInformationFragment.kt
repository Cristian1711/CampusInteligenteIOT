package com.example.campusinteligenteiot.ui.home.events.eventInfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.EventInformationFragmentBinding
import com.example.campusinteligenteiot.databinding.HomeIOTFragmentBinding
import com.example.campusinteligenteiot.ui.home.events.manageIOT.HomeIOTViewModel

class EventInformationFragment : Fragment() {

    private  var _binding: EventInformationFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EventInformationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventInformationFragmentBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}