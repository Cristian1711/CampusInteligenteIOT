package com.example.campusinteligenteiot.usecases.product

import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductCall
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.repository.EventRepository
import com.example.campusinteligenteiot.repository.ProductRepository
import retrofit2.Response

class SaveProductUseCase {
    private val repository = ProductRepository()

    suspend operator fun invoke(id: String?, product: ProductCall): Response<String> = repository.saveProduct(product, id)
}