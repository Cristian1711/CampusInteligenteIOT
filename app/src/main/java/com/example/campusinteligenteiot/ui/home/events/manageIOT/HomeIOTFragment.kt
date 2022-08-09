package com.example.campusinteligenteiot.ui.home.events.manageIOT

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.campusinteligenteiot.R

class HomeIOTFragment : Fragment() {

    companion object {
        fun newInstance() = HomeIOTFragment()
    }

    private lateinit var viewModel: HomeIOTViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_i_o_t_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeIOTViewModel::class.java)
        // TODO: Use the ViewModel
    }

}