package com.example.campusinteligenteiot.ui.home.shop

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.product.GetAllProductsUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase

class ShopViewModel : ViewModel() {
    val searchUserUseCase = SearchUserUseCase()
    val getAllProductsUseCase = GetAllProductsUseCase()

    suspend fun getAllProducts(): List<ProductResponse>?{
        return getAllProductsUseCase()
    }

    suspend fun getUser(id: String): UsersResponse?{
        return searchUserUseCase(id)
    }
}