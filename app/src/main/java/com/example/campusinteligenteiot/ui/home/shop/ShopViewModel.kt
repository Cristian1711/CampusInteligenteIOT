package com.example.campusinteligenteiot.ui.home.shop

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.usecases.product.GetAllProductsUseCase

class ShopViewModel : ViewModel() {
    val getAllProductsUseCase = GetAllProductsUseCase()

    suspend fun getAllProducts(): List<ProductResponse>?{
        return getAllProductsUseCase()
    }
}