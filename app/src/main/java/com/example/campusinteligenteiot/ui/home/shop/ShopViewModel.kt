package com.example.campusinteligenteiot.ui.home.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.model.Product
import com.example.campusinteligenteiot.usecases.product.GetAllProductsUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase

class ShopViewModel : ViewModel() {
    val searchUserUseCase = SearchUserUseCase()
    val getAllProductsUseCase = GetAllProductsUseCase()
    lateinit var productListFiltered: MutableList<ProductResponse>

    suspend fun getAllPublishedProducts(): LiveData<MutableList<ProductResponse>> {
        val mutableData = MutableLiveData<MutableList<ProductResponse>>()
        getAllProductsUseCase()?.observeForever{ productList ->
            productListFiltered = (productList.filter { it.published }) as MutableList<ProductResponse>
            mutableData.value = productListFiltered
        }
        return mutableData
    }

    suspend fun getUser(id: String): UsersResponse?{
        return searchUserUseCase(id)
    }
}