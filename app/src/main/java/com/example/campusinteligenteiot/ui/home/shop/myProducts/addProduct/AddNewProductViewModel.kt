package com.example.campusinteligenteiot.ui.home.shop.myProducts.addProduct

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.usecases.product.SaveProductUseCase

class AddNewProductViewModel : ViewModel() {
    val saveProductUseCase = SaveProductUseCase()

    suspend fun saveProduct(id: String?, product: ProductResponse){
        saveProductUseCase(id, product)
    }
}