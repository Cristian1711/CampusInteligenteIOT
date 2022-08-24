package com.example.campusinteligenteiot.ui.home.car.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.view.View.GONE
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ItemEventBinding
import com.example.campusinteligenteiot.databinding.TripItemBinding
import com.example.campusinteligenteiot.usecases.event.SaveEventUseCase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TripViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val saveEventUseCase = SaveEventUseCase()
    val binding = TripItemBinding.bind(view)

    fun render(trip: TripResponse, user: UsersResponse,onClickDelete: (Int) -> Unit){
        if(!trip.driver.equals(user.id)){
            binding.deleteTripButton.visibility = GONE
            binding.checkButton.visibility = GONE
        }
        val dateString = toStringWithTime(trip.departureDate)
        binding.driverName.text = binding.driverName.text.toString() + user.userName
        binding.tripDate.text = dateString
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(user.profileImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(binding.driverImage)
        }

        binding.showTripButton.setOnClickListener{
            val bundle = bundleOf(
                "tripId" to trip.id
            )
            val navController = Navigation.findNavController(this.itemView)
            navController.navigate(R.id.action_navigation_events_to_eventDetail, bundle)
        }

        binding.deleteTripButton.setOnClickListener {
            onClickDelete(adapterPosition)
        }

    }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    fun toStringWithTime(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)
    }

}