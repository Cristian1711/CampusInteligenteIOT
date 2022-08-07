package com.example.campusinteligenteiot.ui.home.events.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.databinding.ItemProductBinding
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase
import com.google.firebase.storage.FirebaseStorage

class EventViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val getUserFromLocalUseCase = GetUserFromLocalUseCase()
    val binding = ItemProductBinding.bind(view)
    var like = false

    fun render(event: EventResponse, onClickListener:(EventResponse) -> Unit){
        binding.productTitle.text = event.description
        binding.productPrice.text = event.eventDate
        binding.productOwner.text = event.id
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(event.eventImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(binding.productImage)
        }
        binding.likeImageView.setOnClickListener{
            like = likeAnimation(binding.likeImageView, R.raw.bandai_dokkan, like)
        }
    }

    private fun likeAnimation(imageView: LottieAnimationView, animation: Int, like: Boolean): Boolean {
        if(!like){
            imageView.setAnimation(animation)
            imageView.playAnimation()
        }else{
            imageView.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(object: AnimatorListenerAdapter(){

                    override fun onAnimationEnd(animation: Animator?) {
                        imageView.setImageResource(R.drawable.twitter_like)
                        imageView.alpha = 1f
                    }

                })

        }
        return !like
    }
}