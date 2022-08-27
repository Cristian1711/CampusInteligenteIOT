package com.example.campusinteligenteiot.ui.home.car.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RatingBar
import android.widget.Toast
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
import kotlinx.android.synthetic.main.generic_dialog_1_button.view.*
import kotlinx.android.synthetic.main.star_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TripViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val saveEventUseCase = SaveEventUseCase()
    val binding = TripItemBinding.bind(view)
    val saveTripUseCase = SaveTripUseCase()
    var currentDate = Date()
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
            binding.rateTripButton.visibility = VISIBLE
        }
        else{
            if(!trip.available){
                binding.checkButton.visibility = GONE
            }
        }
        val dateString = toStringWithTime(trip.departureDate)
        binding.driverName.text = binding.driverName.text.toString() + " ${user.userName}"
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

        binding.rateTripButton.setOnClickListener{
            if(trip.passengers.contains(user.id) && !trip.available){
                rateDriver()
            }
        }

        binding.checkButton.setOnClickListener{
            if(getZeroTimeDate(trip.departureDate).compareTo(getZeroTimeDate(currentDate)) > 0){
                val builder = AlertDialog.Builder(context)
                val myView = LayoutInflater.from(context).inflate(R.layout.generic_dialog_1_button, null)
                builder.setView(myView)
                val dialog = builder.create()

                myView.Question.text = context.getString(R.string.cant_close_trip)
                myView.Question2.text = context.getString(R.string.condition)

                dialog.show()

                myView.cancelButton.setOnClickListener{
                    dialog.cancel()
                }
            }
            else{
                GlobalScope.launch(Dispatchers.Main){
                    trip.available = false
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
                    val builder = AlertDialog.Builder(context)
                    val myView = LayoutInflater.from(context).inflate(R.layout.generic_dialog_1_button, null)
                    builder.setView(myView)
                    val dialog = builder.create()

                    myView.Question.text = context.getString(R.string.closed_trip)
                    myView.Question2.text = context.getString(R.string.question2_dialog1_generic)

                    dialog.show()

                    myView.cancelButton.setOnClickListener {
                        dialog.cancel()
                    }

                }
            }
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
                    val builder = AlertDialog.Builder(context)
                    val myView = LayoutInflater.from(context).inflate(R.layout.generic_dialog_1_button, null)
                    builder.setView(myView)
                    val dialog = builder.create()

                    myView.Question.text = context.getString(R.string.not_more_seats)
                    myView.Question2.text = context.getString(R.string.search_trip)

                    dialog.show()

                    myView.cancelButton.setOnClickListener{
                        dialog.cancel()
                    }
                }

            }
        }

    }

    fun getZeroTimeDate(date:Date): Date {
        var res: Date = date
        val calendar = Calendar.getInstance()

        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        res = calendar.time

        return res
    }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    fun toStringWithTime(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)
    }

    fun rateDriver() {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.star_dialog, null)
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
                message = context.getString(R.string.sorry_text)
            }
            2 -> {
                message = context.getString(R.string.sorry_text_2)
            }
            3 -> {
                message = context.getString(R.string.thank_you)
            }
            4 -> {
                message = context.getString(R.string.awesome)
            }
            else -> {
                message = context.getString(R.string.thank_you_2)
            }
        }

        return message
    }

}