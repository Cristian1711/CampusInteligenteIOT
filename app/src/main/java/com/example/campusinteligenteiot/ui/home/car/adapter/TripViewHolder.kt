package com.example.campusinteligenteiot.ui.home.car.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.trip.TripCall
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ItemEventBinding
import com.example.campusinteligenteiot.databinding.TripItemBinding
import com.example.campusinteligenteiot.usecases.event.SaveEventUseCase
import com.example.campusinteligenteiot.usecases.trip.SaveTripUseCase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.delete_dialog_1.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TripViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val saveEventUseCase = SaveEventUseCase()
    val binding = TripItemBinding.bind(view)
    val saveTripUseCase = SaveTripUseCase()
    private lateinit var current_user: UsersResponse

    fun render(trip: TripResponse, user: UsersResponse,onClickDelete: (Int) -> Unit){
        val sharedPreferences = context.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        current_user = gson.fromJson(json, UsersResponse::class.java)

        if(!trip.driver.equals(current_user.id)){
            binding.deleteTripButton.visibility = GONE
            binding.checkButton.visibility = GONE
            binding.addToTripButton.visibility = VISIBLE
        }
        else{
            if(!trip.available){
                binding.checkButton.visibility = GONE
            }
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
            navController.navigate(R.id.action_carDriverFragment_to_tripDetailsFragment, bundle)
        }

        binding.deleteTripButton.setOnClickListener {
            onClickDelete(adapterPosition)
        }

        binding.addToTripButton.setOnClickListener{
            GlobalScope.launch(Dispatchers.Main) {
                if(trip.passengers.size < trip.seats){
                    trip.passengers.add(current_user.id)
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

                    saveTripUseCase(trip.id, tripCall)
                }
                else{

                }

            }
        }

    }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    fun toStringWithTime(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)
    }

}