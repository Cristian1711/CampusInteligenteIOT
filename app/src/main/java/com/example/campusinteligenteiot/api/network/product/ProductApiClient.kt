package com.example.campusinteligenteiot.api.network.product

import com.example.campusinteligenteiot.api.model.product.ProductResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductApiClient {
    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/products/getProduct/{id}")
    suspend fun getProductById(@Path("id") id:String): Response<ProductResponse>

    @Headers("Content-Type: application/json")
    @GET("http://149.91.99.198:8080/api/v1/products/allProducts")
    suspend fun getAllProducts(): Response<List<ProductResponse>>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/products/saveProduct/{id}")
    suspend fun saveProduct(@Body params: ProductResponse, @Path("id") id: String): Response<String>

    @Headers("Content-Type: application/json")
    @POST("http://149.91.99.198:8080/api/v1/products/deleteProduct/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<ProductResponse>
}