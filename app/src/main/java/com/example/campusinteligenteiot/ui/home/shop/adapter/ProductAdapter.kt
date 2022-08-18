package com.example.campusinteligenteiot.ui.home.shop.adapter

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse

class ProductAdapter(private val user: UsersResponse, private val context: Context, private val myProducts: Boolean)
    : RecyclerView.Adapter<RecyclerView.ViewHolder> (){

    private var productMutableList = mutableListOf<ProductResponse>()
    private var oldProductMutableList = mutableListOf<ProductResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if(myProducts){
            return MyProductViewHolder(layoutInflater.inflate(R.layout.item_my_product, parent, false), context)
        }
        else{
            return ProductViewHolder(layoutInflater.inflate(R.layout.item_my_product, parent, false), context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = productMutableList[position]
        if(myProducts){
            val realHolder = holder as MyProductViewHolder
            realHolder.render(user, item)
        }else{
            val realHolder = holder as ProductViewHolder
            realHolder.render(user, item)
        }
    }


    override fun getItemCount(): Int = productMutableList.size

    fun setProductList(data: MutableList<ProductResponse>) {
        productMutableList = data
        oldProductMutableList = productMutableList
    }

    fun filterProductList(category: String){
        if(category == "Sin filtro"){
            productMutableList = (oldProductMutableList.filter { it.category != category}) as MutableList<ProductResponse>
        }
        else{
            productMutableList = (oldProductMutableList.filter { it.category == category}) as MutableList<ProductResponse>
        }
    }

    fun filterProductListByLikes(user: UsersResponse){
        productMutableList = (oldProductMutableList.filter { user.productLikes.contains(it.id)}) as MutableList<ProductResponse>

        println("LIKES")
        println(productMutableList)
    }

    fun filterProductListByOwner(userId: String){
        productMutableList = (oldProductMutableList.filter { it.idOwner == userId}) as MutableList<ProductResponse>
    }

}