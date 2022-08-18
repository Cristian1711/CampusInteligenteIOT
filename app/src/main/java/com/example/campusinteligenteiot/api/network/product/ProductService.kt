package com.example.campusinteligenteiot.api.network.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.campusinteligenteiot.api.config.RetrofitBuilder
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.api.network.user.UserApiClient
import com.example.campusinteligenteiot.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductService {

    private val retrofit = RetrofitBuilder.getRetrofit()

    suspend fun getProductById(id: String): ProductResponse {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(ProductApiClient::class.java).getProductById(id)
            response.body()!!
        }
    }

    suspend fun getAllProducts(): LiveData<MutableList<ProductResponse>> {
        val mutableData = MutableLiveData<MutableList<ProductResponse>>()
        var dataList: MutableList<ProductResponse>
        return withContext(Dispatchers.IO){
            dataList = (retrofit.create(ProductApiClient::class.java).getAllProducts()) as MutableList<ProductResponse>
            mutableData.postValue(dataList)
            mutableData
        }
    }

    suspend fun saveProduct(product: ProductResponse, id: String) : Response<String> {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(ProductApiClient::class.java).saveProduct(product, id)
            println("he conseguido una respuesta")
            response
        }
    }

    suspend fun deleteProduct(id: String) : ProductResponse {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(ProductApiClient::class.java).deleteProduct(id)
            println("el producto eliminado")
            response.body()!!
        }
    }
}