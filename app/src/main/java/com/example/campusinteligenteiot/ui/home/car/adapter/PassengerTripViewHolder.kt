package com.example.campusinteligenteiot.ui.home.car.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.trip.TripCall
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.PassengerTripItemBinding
import com.example.campusinteligenteiot.databinding.TripItemBinding
import com.example.campusinteligenteiot.usecases.event.SaveEventUseCase
import com.example.campusinteligenteiot.usecases.trip.SaveTripUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.generic_dialog_1_button.view.*
import kotlinx.android.synthetic.main.star_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PassengerTripViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val saveEventUseCase = SaveEventUseCase()
    val binding = PassengerTripItemBinding.bind(view)
    val searchUserUseCase = SearchUserUseCase()
    val saveTripUseCase = SaveTripUseCase()
    var currentDate = Date()
    private lateinit var driverUser: UsersResponse

    fun render(trip: TripResponse, user: UsersResponse, onClickDelete: (Int) -> Unit){

        val dateString = toStringWithTimeText(trip.departureDate)
        binding.tripDate.text = dateString

        GlobalScope.launch(Dispatchers.Main){
            driverUser = searchUserUseCase(trip.driver)!!
            binding.driverName.text = binding.driverName.text.toString() + " ${driverUser.userName}"
            val storageReference = FirebaseStorage.getInstance()
            val gsReference = storageReference.getReferenceFromUrl(driverUser.profileImage)
            gsReference.downloadUrl.addOnSuccessListener {
                Glide.with(context).load(it).into(binding.driverImage)
            }
        }

        binding.showTripButton.setOnClickListener{
            val bundle = bundleOf(
                "tripId" to trip.id
            )
            val navController = Navigation.findNavController(this.itemView)
            navController.navigate(R.id.action_carPassengerFragment_to_tripDetailsFragment, bundle)
        }



        binding.rateTripButton.setOnClickListener{
            if(trip.passengers.contains(user.id) && !trip.available){
                rateDriver()
            }
            else if (!trip.passengers.contains(user.id)){
                val builder = AlertDialog.Builder(context)
                val myView = LayoutInflater.from(context).inflate(R.layout.generic_dialog_1_button, null)
                builder.setView(myView)
                val dialog = builder.create()

                myView.Question.text = context.getString(R.string.not_passenger)
                myView.Question2.text = context.getString(R.string.rate_condition)

                dialog.show()

                myView.cancelButton.setOnClickListener {
                    dialog.cancel()
                }
            }
            else if (trip.passengers.contains(user.id) && trip.available){
                val builder = AlertDialog.Builder(context)
                val myView = LayoutInflater.from(context).inflate(R.layout.generic_dialog_1_button, null)
                builder.setView(myView)
                val dialog = builder.create()

                myView.Question.text = context.getString(R.string.cant_rate)
                myView.Question2.text = context.getString(R.string.condition_rating_trip)

                dialog.show()

                myView.cancelButton.setOnClickListener {
                    dialog.cancel()
                }
            }
        }

        binding.addToTripButton.setOnClickListener{
            GlobalScope.launch(Dispatchers.Main) {
                if(trip.passengers.size < trip.seats) {
                    if (!trip.passengers.contains(user.id)) {
                        trip.passengers.add(user.id)
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
                        val myView = LayoutInflater.from(context)
                            .inflate(R.layout.generic_dialog_1_button, null)
                        builder.setView(myView)
                        val dialog = builder.create()

                        myView.Question.text = context.getString(R.string.add_to_trip)
                        myView.Question2.text = context.getString(R.string.contact_driver)

                        dialog.show()

                        myView.cancelButton.setOnClickListener {
                            dialog.cancel()
                        }

                    } else{
                        val builder = AlertDialog.Builder(context)
                        val myView = LayoutInflater.from(context)
                            .inflate(R.layout.generic_dialog_1_button, null)
                        builder.setView(myView)
                        val dialog = builder.create()

                        myView.Question.text = context.getString(R.string.already_passenger)
                        myView.Question2.text = context.getString(R.string.contact_driver)

                        dialog.show()

                        myView.cancelButton.setOnClickListener {
                            dialog.cancel()
                        }

                    }
                    } else {
                        val builder = AlertDialog.Builder(context)
                        val myView = LayoutInflater.from(context)
                            .inflate(R.layout.generic_dialog_1_button, null)
                        builder.setView(myView)
                        val dialog = builder.create()

                        myView.Question.text = context.getString(R.string.not_more_seats)
                        myView.Question2.text = context.getString(R.string.search_trip)

                        dialog.show()

                        myView.cancelButton.setOnClickListener {
                            dialog.cancel()
                        }
                    }

            }
        }

    }

    fun toStringWithTime(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(this)
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
                message = context.getString(R.string.sorry_text_driver)
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
                message = context.getString(R.string.ole_text)
            }
        }

        return message
    }

    fun toStringWithTimeText(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("dd-MM-yyyy HH:mm").format(this)
    }

}