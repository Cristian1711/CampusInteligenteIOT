package com.example.campusinteligenteiot.ui.home.shop.myProducts.editProduct

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.usecases.product.DeleteProductUseCase
import com.example.campusinteligenteiot.usecases.product.SaveProductUseCase

class EditProductViewModel : ViewModel() {
    val saveProductUseCase = SaveProductUseCase()

    suspend fun saveProduct(product: ProductResponse){
        saveProductUseCase(product.id, product)
    }

}