package com.example.campusinteligenteiot.usecases.product

import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.repository.ProductRepository
import retrofit2.Response

class DeleteProductUseCase {
    private val repository = ProductRepository()

    suspend operator fun invoke(id: String): Response<ProductResponse> = repository.deleteProduct(id)
}