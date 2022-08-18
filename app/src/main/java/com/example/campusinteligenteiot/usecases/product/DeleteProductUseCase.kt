package com.example.campusinteligenteiot.usecases.product

import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.repository.ProductRepository

class DeleteProductUseCase {
    private val repository = ProductRepository()

    suspend operator fun invoke(id: String): ProductResponse = repository.deleteProduct(id)
}