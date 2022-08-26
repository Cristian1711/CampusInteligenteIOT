package com.example.campusinteligenteiot.ui.home.car.passenger

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.CarDriverFragmentBinding
import com.example.campusinteligenteiot.databinding.CarPassengerFragmentBinding
import com.example.campusinteligenteiot.ui.home.car.adapter.TripAdapter
import com.example.campusinteligenteiot.ui.home.car.driver.CarDriverViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CarPassengerFragment : Fragment() {

    private val viewModel by viewModels<CarPassengerViewModel>()
    private var _binding: CarPassengerFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUser: UsersResponse
    private lateinit var user: UsersResponse
    private lateinit var adapter: TripAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CarPassengerFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        currentUser = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main){
            observeData()
        }

        return binding.root
    }

    private suspend fun observeData() {
        viewModel.getAllTrips().observe(viewLifecycleOwner, Observer{
            adapter.setTripList(it)
            adapter.filterTripListVisible()
            adapter.notifyItemInserted(it.size-1)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTripRecyclerView(view)
    }

    private fun initTripRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.tripRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TripAdapter(user, requireContext(),
            {position -> onDeletedItem(position)}
        )
        recyclerView.adapter = adapter
    }

    private fun onDeletedItem(position: Int) {

    }

}