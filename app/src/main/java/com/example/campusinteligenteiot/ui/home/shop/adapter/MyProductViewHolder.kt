package com.example.campusinteligenteiot.ui.home.shop.adapter

import android.content.Context
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ItemMyProductBinding
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase
import com.example.campusinteligenteiot.usecases.user.SaveUserUseCase
import com.google.firebase.storage.FirebaseStorage

class MyProductViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view) {

    val getUserFromLocalUseCase = GetUserFromLocalUseCase()
    val saveUserUseCase = SaveUserUseCase()
    val binding = ItemMyProductBinding.bind(view)
    val myView = view

    fun render(
        user: UsersResponse,
        product: ProductResponse,
        onClickDelete: (Int) -> Unit
    ){
        println("HE ENTRADO EN MYPRODUCT VIEWHOLDER")
        binding.productTitle.text = product.title
        binding.productPrice.text = product.price.toString() + "€"
        if(product.published){
            binding.IsPublished.text = "Publicado"
        }
        else{
            if(product.archived){
                binding.IsPublished.text = "Archivado"
            }
            else{
                binding.IsPublished.text = "No publicado"
            }
        }
        val storageReference = FirebaseStorage.getInstance()
        println("LA IMAGEN QUE HAY ES ${product.productImage}")
        val gsReference = storageReference.getReferenceFromUrl(product.productImage!!)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(context).load(it).into(binding.productImage)
        }

        binding.EditImage.setOnClickListener{
            val bundle = bundleOf(
                "productId" to product.id,
                "productTitle" to product.title,
                "productDescription" to product.description,
                "productPrice" to product.price,
                "productImage" to product.productImage,
                "category" to product.category,
                "idOwner" to product.idOwner,
                "published" to product.published,
                "archived" to product.archived
            )
            val navController = Navigation.findNavController(myView)
            navController.navigate(R.id.action_myProductsFragment_to_editProductFragment, bundle)
        }

        binding.DeleteImage.setOnClickListener{
            onClickDelete(adapterPosition)
        }

    }


}