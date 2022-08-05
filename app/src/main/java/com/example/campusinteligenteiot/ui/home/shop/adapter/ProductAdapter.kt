package com.example.campusinteligenteiot.ui.home.shop.adapter

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductResponse

class ProductAdapter(private val productList: List<ProductResponse>, private val context: Context,
private val onClickListener:(ProductResponse) -> Unit): RecyclerView.Adapter<ProductViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false), context)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int = productList.size
}