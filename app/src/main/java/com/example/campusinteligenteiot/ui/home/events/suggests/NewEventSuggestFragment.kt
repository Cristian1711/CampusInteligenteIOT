package com.example.campusinteligenteiot.ui.home.events.suggests

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.campusinteligenteiot.R

class NewEventSuggestFragment : Fragment() {

    companion object {
        fun newInstance() = NewEventSuggestFragment()
    }

    private lateinit var viewModel: NewEventSuggestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.new_event_suggest_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewEventSuggestViewModel::class.java)
        // TODO: Use the ViewModel
    }

}