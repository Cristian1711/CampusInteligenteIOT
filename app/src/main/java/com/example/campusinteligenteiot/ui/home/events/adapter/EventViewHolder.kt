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
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ItemEventBinding
import com.example.campusinteligenteiot.databinding.ItemProductBinding
import com.example.campusinteligenteiot.usecases.event.SaveEventUseCase
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val saveEventUseCase = SaveEventUseCase()
    val binding = ItemEventBinding.bind(view)

    fun render(event: EventResponse, user: UsersResponse){
        var willAssist: Boolean
        willAssist = event.attendances.contains(user.id)
        if(willAssist) binding.starImage.setImageResource(R.drawable.ic_star_yellow)
        binding.eventTitle.text = event.eventTitle
        binding.eventPlace.text = event.eventPlace
        binding.eventDatePlaceHolder.text = binding.eventDatePlaceHolder.text.toString() + ": ${event.eventDate.day}/${event.eventDate.month}/${event.eventDate.year}"
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(event.eventImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(binding.eventImage)
        }

        binding.showDetailButton.setOnClickListener{
            val bundle = bundleOf(
                "eventId" to event.id
            )
            val navController = Navigation.findNavController(this.itemView)
            navController.navigate(R.id.action_navigation_events_to_eventDetail, bundle)
        }

        binding.starImage.setOnClickListener{
            willAssist = starAnimation(binding.starImage, R.raw.bandai_dokkan, willAssist)
            if(willAssist){
                event.attendances.add(user.id)
                GlobalScope.launch(Dispatchers.Main) {
                    saveEventUseCase(event.id, event)
                }
            } else{
                event.attendances.remove(user.id)
                GlobalScope.launch(Dispatchers.Main) {
                    saveEventUseCase(event.id, event)
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

}