package com.example.campusinteligenteiot.ui.home.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.product.GetAllProductsUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase

class ShopViewModel : ViewModel() {
    val searchUserUseCase = SearchUserUseCase()
    val getAllProductsUseCase = GetAllProductsUseCase()

    suspend fun getAllProducts(): LiveData<MutableList<ProductResponse>> {
        val mutableData = MutableLiveData<MutableList<ProductResponse>>()
        getAllProductsUseCase()?.observeForever{ productList ->
            mutableData.value = productList
        }
        return mutableData
    }

    suspend fun getUser(id: String): UsersResponse?{
        return searchUserUseCase(id)
    }
}