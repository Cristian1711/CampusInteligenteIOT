package com.example.campusinteligenteiot.ui.home.shop.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ItemMyProductBinding
import com.example.campusinteligenteiot.databinding.ItemProductBinding
import com.example.campusinteligenteiot.model.Product
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase
import com.example.campusinteligenteiot.usecases.user.SaveUserUseCase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyProductViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val getUserFromLocalUseCase = GetUserFromLocalUseCase()
    val saveUserUseCase = SaveUserUseCase()
    val binding = ItemMyProductBinding.bind(view)

    fun render(user: UsersResponse, product: ProductResponse){
        println("HE ENTRADO EN MYPRODUCT VIEWHOLDER")
        binding.EditImage.visibility = VISIBLE
        binding.DeleteImage.visibility = VISIBLE
        binding.likeImageView.visibility = INVISIBLE
        binding.productTitle.text = product.title
        binding.productPrice.text = product.price.toString() + "€"
        binding.productOwner.visibility = INVISIBLE
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(product.productImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(binding.productImage)
        }

        binding.EditImage.setOnClickListener{

        }

        binding.DeleteImage.setOnClickListener{

        }

    }


}