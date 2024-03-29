package com.example.campusinteligenteiot.ui.home.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.common.adapter.SectionPagerAdapter
import com.example.campusinteligenteiot.databinding.ScheduleFragmentBinding
import com.example.campusinteligenteiot.ui.home.schedule.bus.BusScheduleFragment
import com.example.campusinteligenteiot.ui.home.schedule.train.TrainScheduleFragment
import com.google.android.material.tabs.TabLayout

class ScheduleFragment : Fragment() {

    private  var _binding: ScheduleFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ScheduleViewModel>()
    private lateinit var myFragment: View
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ScheduleFragmentBinding.inflate(inflater,container,false)
        viewPager = binding.root.findViewById(R.id.viewPager)
        tabLayout = binding.root.findViewById(R.id.tabLayout)
        addFragments()
        return binding.root
    }

    private fun addFragments() {
        val adapter = SectionPagerAdapter(childFragmentManager)
        adapter.addFragment(BusScheduleFragment(), "Horario Autobús")
        adapter.addFragment(TrainScheduleFragment(), "Horario Tren")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

    }

}