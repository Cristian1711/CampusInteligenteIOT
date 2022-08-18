package com.example.campusinteligenteiot.ui.home.shop.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
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

class ProductViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val getUserFromLocalUseCase = GetUserFromLocalUseCase()
    val saveUserUseCase = SaveUserUseCase()
    val binding = ItemMyProductBinding.bind(view)

    fun render(user: UsersResponse, product: ProductResponse){
        println("HE ENTRADO EN PRODUCT VIEWHOLDER")
        var like: Boolean
        like = user.productLikes.contains(product.id)
        if(like) binding.likeImageView.setImageResource(R.drawable.ic_twitter_like)
        binding.productTitle.text = product.title
        binding.productPrice.text = product.price.toString() + "€"
        binding.productOwner.text = "by " + getUserFromLocalUseCase(product.idOwner)?.userName ?: "Usuario no especificado"
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(product.productImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(binding.productImage)
        }

        binding.productImage.setOnClickListener{
                val bundle = bundleOf(
                    "productId" to product.id
                )
                val navController = Navigation.findNavController(this.itemView)
                navController.navigate(R.id.action_navigation_shop_to_productDetailFragment, bundle)
        }

        binding.likeImageView.setOnClickListener{
            println("HE PULSADO EL BOTON DE LIKE")
            println(like)
            like = likeAnimation(binding.likeImageView, R.raw.bandai_dokkan, like)
            if(like){
                user.productLikes.add(product.id)
                GlobalScope.launch(Dispatchers.Main) {
                    saveUserUseCase(user.id, user)
                    val sharedPreferences = context.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    val json = gson.toJson(user)
                    editor.putString("current_user", json)
                    editor.commit()
                }
            } else{
                user.productLikes.remove(product.id)
                GlobalScope.launch(Dispatchers.Main) {
                    saveUserUseCase(user.id, user)
                    val sharedPreferences = context.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    val json = gson.toJson(user)
                    editor.putString("current_user", json)
                    editor.commit()
                }
            }
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