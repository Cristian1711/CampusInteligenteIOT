package com.example.campusinteligenteiot.ui.home.events.eventInfo

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.EventInformationFragmentBinding
import com.example.campusinteligenteiot.databinding.HomeIOTFragmentBinding
import com.example.campusinteligenteiot.ui.drawer.friends.UsersAdapter
import com.example.campusinteligenteiot.ui.home.events.manageIOT.HomeIOTViewModel
import com.example.campusinteligenteiot.ui.home.shop.adapter.ProductAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.star_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventInformationFragment : Fragment() {

    private  var _binding: EventInformationFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EventInformationViewModel>()
    private lateinit var user: UsersResponse
    private lateinit var event: EventResponse
    private lateinit var adapter: UsersAdapter
    private lateinit var recyclerView: RecyclerView
    private var eventId: String? = null
    private lateinit var eventCall: EventCall

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventInformationFragmentBinding.inflate(inflater,container,false)

        eventId = arguments?.getString("eventId")
        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main){
            event = viewModel.getSingleEvent(eventId!!)
            observeData(eventId!!)
        }

        return binding.root
    }

    private suspend fun observeData(id: String) {
        viewModel.getEvent(id).observe(viewLifecycleOwner, Observer{
            val assistantsList = ArrayList<UsersResponse>()
            GlobalScope.launch(Dispatchers.Main) {
                for (id in it.assistants) {
                    assistantsList.add(viewModel.getUser(id))
                }
                adapter.setAssistantsList(assistantsList)
                adapter.filterAssistantsList(user.id)
                adapter.notifyDataSetChanged()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUsersRecyclerView(view)

        binding.backButton.setOnClickListener{
            showAlertDialog()
        }

        binding.starImage.setOnClickListener{
            rateEvent()
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            GlobalScope.launch(Dispatchers.Main){
                observeData(eventId!!)
            }

        }
    }

    fun rateEvent() {
        val builder = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.star_dialog, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()


        val average: Float
        val ratingBar = dialog.rBar
        val button = dialog.button

        ratingBar.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                Toast.makeText(context, ratingMessage(rating), Toast.LENGTH_LONG).show()
            }

        button.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                //añadir rating por hacer
                dialog.hide()
            }
        }
    }

    fun ratingMessage(float: Float): String {

        var message = "not selected"
        val int = float.toInt()

        when (int) {
            1 -> {
                message = getString(R.string.sorry_text)
            }
            2 -> {
                message = getString(R.string.sorry_text_2)
            }
            3 -> {
                message = getString(R.string.thank_you)
            }
            4 -> {
                message = getString(R.string.awesome)
            }
            else -> {
                message = getString(R.string.thank_you_2)
            }
        }

        return message
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle(getString(R.string.out_event))
            setMessage(getString(R.string.out_event_question))
            setPositiveButton("Sí") { dialog: DialogInterface, _: Int ->
                event.assistants.remove(user.id)
                eventCall = EventCall(
                    event.assistants,
                    event.attendances,
                    toSimpleString(event.eventDate),
                    event.description,
                    event.eventImage,
                    event.eventTitle,
                    event.eventPlace,
                    event.suggested
                )

                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.saveEvent(event.id, eventCall)
                    findNavController().navigate(R.id.action_eventInformationFragment_to_navigation_events)
                    dialog.dismiss()
                }
            }
            setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
        }.create().show()
    }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    private fun initUsersRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.userRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UsersAdapter(user, requireContext(), true)
        recyclerView.adapter = adapter
    }

}