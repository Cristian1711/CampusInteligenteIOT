package com.example.campusinteligenteiot.ui.home.shop.myProducts.addProduct

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.product.ProductCall
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.usecases.product.GetSingleProductUseCase
import com.example.campusinteligenteiot.usecases.product.SaveProductUseCase
import retrofit2.Response

class AddNewProductViewModel : ViewModel() {
    val saveProductUseCase = SaveProductUseCase()

    suspend fun saveProduct(id: String?, product: ProductCall): Response<String> {
        return saveProductUseCase(id, product)
    }

}