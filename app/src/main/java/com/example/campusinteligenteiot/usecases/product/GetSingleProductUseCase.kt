package com.example.campusinteligenteiot.usecases.product

import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.repository.ProductRepository

class GetSingleProductUseCase {
    private val repository = ProductRepository()

    suspend operator fun invoke(id:String): ProductResponse? = repository.getProductById(id)
}