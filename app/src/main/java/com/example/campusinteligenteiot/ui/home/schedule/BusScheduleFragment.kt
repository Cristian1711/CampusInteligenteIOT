package com.example.campusinteligenteiot.ui.home.schedule

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.campusinteligenteiot.R

class BusScheduleFragment : Fragment() {

    companion object {
        fun newInstance() = BusScheduleFragment()
    }

    private lateinit var viewModel: BusScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bus_schedule_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BusScheduleViewModel::class.java)
        // TODO: Use the ViewModel
    }

}