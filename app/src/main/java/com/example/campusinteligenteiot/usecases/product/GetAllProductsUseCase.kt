package com.example.campusinteligenteiot.usecases.product

import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.repository.ProductRepository

class GetAllProductsUseCase {
    private val repository = ProductRepository()

    suspend operator fun invoke(): List<ProductResponse>? = repository.getAllProducts()
}