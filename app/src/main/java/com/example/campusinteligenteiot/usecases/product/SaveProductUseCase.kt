package com.example.campusinteligenteiot.usecases.product

import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.repository.EventRepository
import com.example.campusinteligenteiot.repository.ProductRepository

class SaveProductUseCase {
    private val repository = ProductRepository()

    suspend operator fun invoke(id: String, product: ProductResponse) = repository.saveProduct(product, id)
}