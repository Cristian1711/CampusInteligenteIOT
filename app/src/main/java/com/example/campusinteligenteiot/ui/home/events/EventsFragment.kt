package com.example.campusinteligenteiot.ui.home.events

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.EventsFragmentBinding
import com.example.campusinteligenteiot.databinding.ShopFragmentBinding
import com.example.campusinteligenteiot.ui.home.events.adapter.EventAdapter
import com.example.campusinteligenteiot.ui.home.shop.ShopViewModel
import com.example.campusinteligenteiot.ui.home.shop.adapter.ProductAdapter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventsFragment : Fragment() {

    private  var _binding: EventsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EventsViewModel>()
    private lateinit var user: UsersResponse
    private val translationYaxis = 100f
    private var menuOpen = false
    private val interpolator: OvershootInterpolator = OvershootInterpolator()
    private lateinit var adapter: EventAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventsFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main){
            observeData()
        }

        return binding.root
    }

    private fun showMenu() {
        binding.fabEntryEvent.alpha = 0f
        binding.fabSuggestEvent.alpha = 0f
        binding.cardView2.alpha = 0f
        binding.cardView3.alpha = 0f

        binding.fabSuggestEvent.translationY = translationYaxis
        binding.fabEntryEvent.translationY = translationYaxis
        binding.cardView2.translationY = translationYaxis
        binding.cardView3.translationY = translationYaxis

        binding.fabMain.setOnClickListener{
            if(menuOpen){
                closeMenu()
            }
            else{
                openMenu()
            }
        }

    }

    private fun initEventsRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.eventRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = EventAdapter(user, requireContext())
        recyclerView.adapter = adapter
    }

    private fun openMenu() {
        menuOpen = !menuOpen
        binding.fabEntryEvent.visibility = View.VISIBLE
        binding.fabSuggestEvent.visibility = View.VISIBLE

        binding.cardView2.visibility = View.VISIBLE
        binding.cardView3.visibility = View.VISIBLE

        binding.fabMain.setImageResource(R.drawable.ic_down_arrow)
        binding.fabEntryEvent.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabSuggestEvent.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()

        binding.cardView2.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
        binding.cardView3.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()

        binding.fabEntryEvent.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_events_to_homeIOTFragment)
        }

        binding.fabSuggestEvent.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_events_to_newEventSuggestFragment)
        }

    }

    private fun closeMenu() {
        menuOpen = !menuOpen
        binding.fabMain.setImageResource(R.drawable.ic_arrow_up)
        binding.fabEntryEvent.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabSuggestEvent.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()

        binding.cardView2.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
        binding.cardView3.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()

        Handler().postDelayed({
            binding.fabSuggestEvent.visibility = View.INVISIBLE
            binding.fabEntryEvent.visibility = View.INVISIBLE

            binding.cardView2.visibility = View.INVISIBLE
            binding.cardView3.visibility = View.INVISIBLE
        }, 400)
    }

    private suspend fun observeData() {
        viewModel.getAllEvents().observe(viewLifecycleOwner, Observer{
            adapter.setEventList(it)
            adapter.notifyItemInserted(it.size-1)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEventsRecyclerView(view)
        showMenu()
    }

}