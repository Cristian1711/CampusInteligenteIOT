package com.example.campusinteligenteiot.ui.home.shop.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.databinding.ItemProductBinding
import com.example.campusinteligenteiot.model.Product
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase
import com.google.firebase.storage.FirebaseStorage

class ProductViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val getUserFromLocalUseCase = GetUserFromLocalUseCase()
    val binding = ItemProductBinding.bind(view)
    var like = false

    fun render(product: ProductResponse, onClickListener:(ProductResponse) -> Unit){
        binding.productTitle.text = product.title
        binding.productPrice.text = product.price.toString() + "€"
        binding.productOwner.text = "by " + getUserFromLocalUseCase(product.idOwner!!)?.userName ?: "Usuario no especificado"
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(product.productImage)
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