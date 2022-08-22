package com.example.campusinteligenteiot.ui.home.events.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ItemEventBinding
import com.example.campusinteligenteiot.databinding.ItemProductBinding
import com.example.campusinteligenteiot.usecases.event.SaveEventUseCase
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EventViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val saveEventUseCase = SaveEventUseCase()
    val binding = ItemEventBinding.bind(view)

    fun render(event: EventResponse, user: UsersResponse){
        var willAssist: Boolean
        willAssist = event.attendances.contains(user.id)
        if(willAssist) binding.starImage.setImageResource(R.drawable.ic_star_yellow)
        binding.eventTitle.text = event.eventTitle
        binding.eventPlace.text = event.eventPlace
        val dateString = toSimpleString(event.eventDate)
        binding.eventDatePlaceHolder.text = binding.eventDatePlaceHolder.text.toString() + ": $dateString"
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(event.eventImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(binding.eventImage)
        }

        binding.showDetailButton.setOnClickListener{
            val bundle = bundleOf(
                "eventId" to event.id,
                "willAssist" to willAssist
            )
            val navController = Navigation.findNavController(this.itemView)
            navController.navigate(R.id.action_navigation_events_to_eventDetail, bundle)
        }

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
            willAssist = starAnimation(binding.starImage, R.raw.star_animation, willAssist)
            if(willAssist){
                eventCall.attendances?.add(user.id)
                GlobalScope.launch(Dispatchers.Main) {
                    saveEventUseCase(event.id, eventCall)
                }
            } else{
                eventCall.attendances?.remove(user.id)
                GlobalScope.launch(Dispatchers.Main) {
                    saveEventUseCase(event.id, eventCall)
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

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

}