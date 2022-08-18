package com.example.campusinteligenteiot.repository

import androidx.lifecycle.LiveData
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.network.product.ProductService
import retrofit2.Response

class ProductRepository {

    private val api = ProductService()

    suspend fun getProductById(id: String): ProductResponse {
        return api.getProductById(id)
    }

    suspend fun getAllProducts(): LiveData<MutableList<ProductResponse>>? {
        return api.getAllProducts()
    }

    suspend fun saveProduct(product: ProductResponse, id: String) : Response<String> {
        return api.saveProduct(product, id)
    }

    suspend fun deleteProduct(id: String) : ProductResponse {
        return api.deleteProduct(id)
    }

}