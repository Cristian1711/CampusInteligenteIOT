package com.example.campusinteligenteiot.ui.home.shop.myProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.product.ProductCall
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.product.DeleteProductUseCase
import com.example.campusinteligenteiot.usecases.product.GetAllProductsUseCase
import com.example.campusinteligenteiot.usecases.product.SaveProductUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase
import retrofit2.Response

class MyProductsViewModel : ViewModel() {
    val searchUserUseCase = SearchUserUseCase()
    val getAllProductsUseCase = GetAllProductsUseCase()
    val deleteProductUseCase = DeleteProductUseCase()
    val saveProductUseCase = SaveProductUseCase()

    suspend fun getAllProducts(): LiveData<MutableList<ProductResponse>> {
        val mutableData = MutableLiveData<MutableList<ProductResponse>>()
        getAllProductsUseCase()?.observeForever{ productList ->
            mutableData.value = productList
        }
        return mutableData
    }

    suspend fun getUser(id: String): UsersResponse?{
        return searchUserUseCase(id)
    }
    suspend fun deleteProduct(id: String) : Response<ProductResponse> {
        return deleteProductUseCase(id)
    }

    suspend fun saveProduct(id: String, product: ProductCall){
        saveProductUseCase(id, product)
    }

}