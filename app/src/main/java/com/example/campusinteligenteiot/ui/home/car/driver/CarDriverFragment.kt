package com.example.campusinteligenteiot.ui.home.car.driver

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductCall
import com.example.campusinteligenteiot.api.model.trip.TripCall
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.CarDriverFragmentBinding
import com.example.campusinteligenteiot.ui.home.car.adapter.TripAdapter
import com.example.campusinteligenteiot.ui.home.shop.adapter.MyProductAdapter
import com.google.firebase.Timestamp
import com.google.gson.Gson
import kotlinx.android.synthetic.main.delete_dialog_1.view.*
import kotlinx.android.synthetic.main.delete_dialog_1.view.buttonYes
import kotlinx.android.synthetic.main.delete_dialog_2.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CarDriverFragment : Fragment() {

    private val viewModel by viewModels<CarDriverViewModel>()
    private var _binding: CarDriverFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: UsersResponse
    private lateinit var adapter: TripAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CarDriverFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main){
            observeData()
        }

        return binding.root
    }

    private suspend fun observeData() {
        viewModel.getAllTrips().observe(viewLifecycleOwner, Observer{
            adapter.setTripList(it)
            adapter.filterTripListByDriver(user.id)
            adapter.notifyItemInserted(it.size-1)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTripRecyclerView(view)

        binding.fabAddProduct.setOnClickListener{
            findNavController().navigate(R.id.action_carDriverFragment_to_addTripFragment)
        }

        binding.changeButton.setOnClickListener{
            findNavController().navigate(R.id.action_carDriverFragment_to_carPassengerFragment)
        }
    }

    private fun initTripRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.tripRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = TripAdapter(user, requireContext(),
            {position -> onDeletedItem(position)}
        )
        recyclerView.adapter = adapter
    }

    private fun onDeletedItem(position: Int){
        var trip = adapter.getTripList()[position]
        val builder = AlertDialog.Builder(requireContext())

            val myView = layoutInflater.inflate(R.layout.delete_dialog_1, null)

            builder.setView(myView)

            val dialog = builder.create()
            dialog.show()

            myView.Question_text.text = getString(R.string.question_delete_trip)

            myView.buttonYes.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    val tripCall = TripCall(
                        trip.finalPoint,
                        trip.originPoint,
                        trip.passengers,
                        toStringWithTime(trip.departureDate),
                        trip.seats,
                        trip.deleted,
                        trip.driver,
                        trip.available
                    )

                    viewModel.saveTrip(trip.id, tripCall)
                    adapter.notifyItemRemoved(position)
                    dialog.cancel()
                }
            }

            myView.buttonNo.setOnClickListener {
                dialog.cancel()
            }



        }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    fun toStringWithTime(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)
    }

}