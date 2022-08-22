package com.example.campusinteligenteiot.ui.home.events.detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.EventDetailFragmentBinding
import com.example.campusinteligenteiot.databinding.NewEventSuggestFragmentBinding
import com.example.campusinteligenteiot.ui.home.events.suggests.NewEventSuggestViewModel
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EventDetailFragment : Fragment() {
    private var eventId: String? = null
    private  var _binding: EventDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var event: EventResponse
    private val viewModel by viewModels<EventDetailViewModel>()
    private lateinit var currentUser: UsersResponse
    private var willAssist: Boolean? = false
    private lateinit var dateString: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventDetailFragmentBinding.inflate(inflater,container,false)

        eventId = arguments?.getString("eventId")
        willAssist = arguments?.getBoolean("willAssist")
        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        currentUser = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main) {
            event = viewModel.getSingleEvent(eventId!!)
            dateString = toSimpleStringToText(event.eventDate)
            setEventData(event)
            loadImage(event.eventImage)
            prepareStarButton()
        }

        return binding.root
    }

    private fun prepareStarButton() {
        if(willAssist == true) binding.starImage.setImageResource(R.drawable.ic_star_yellow)
        binding.starImage.setOnClickListener{
            val eventCall = EventCall(
                event.assistants,
                event.attendances,
                toSimpleString(event.eventDate),
                event.description,
                event.eventImage,
                event.eventTitle,
                event.eventPlace,
                event.suggested
            )
            willAssist = starAnimation(binding.starImage, R.raw.star_animation, willAssist!!)
            if(willAssist == true){
                binding.StarText.text =
                    getString(R.string.star_text_2) + dateString + "!"
                eventCall.attendances?.add(currentUser.id)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.updateEvent(event.id, eventCall)
                }
            } else{
                binding.StarText.text = getString(R.string.star_text_1)
                eventCall.attendances?.remove(currentUser.id)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.updateEvent(event.id, eventCall)
                }
            }
        }
    }

    private fun starAnimation(imageView: LottieAnimationView, animation: Int, willAssist: Boolean): Boolean {
        if(!willAssist){
            imageView.setAnimation(animation)
            imageView.playAnimation()
        }else{
            imageView.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(object: AnimatorListenerAdapter(){

                    override fun onAnimationEnd(animation: Animator?) {
                        imageView.setImageResource(R.drawable.grey_star)
                        imageView.alpha = 1f
                    }

                })

        }
        return !willAssist
    }

    private fun loadImage(eventImage: String) {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(eventImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it).into(binding.EventImage)
        }
    }

    private fun setEventData(event: EventResponse) {
        binding.attendancesText.text = getString(R.string.assist_text) + "${event.attendances.size}" + getString(R.string.person_text)
        binding.eventTitle.text = event.eventTitle
        binding.PlaceAndDateText.text = event.eventPlace + " - " + dateString
        binding.DescriptionText.text = event.description
        if(willAssist == true){
            binding.StarText.text =
                getString(R.string.star_text_2) + dateString + "!"
        }
        else{
            binding.StarText.text = getString(R.string.star_text_1)
        }
    }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    fun toSimpleStringToText(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("dd-MM-yyyy").format(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener{
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_eventDetail_to_navigation_events)
        }

        binding.AccessButton.setOnClickListener {
            println("LA FECHA DEL EVENTO ES ${event.eventDate}")
        }
    }

}