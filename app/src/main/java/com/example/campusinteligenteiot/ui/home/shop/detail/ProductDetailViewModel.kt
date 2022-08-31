package com.example.campusinteligenteiot.ui.home.shop.detail

import androidx.lifecycle.ViewModel
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.usecases.product.GetSingleProductUseCase
import com.example.campusinteligenteiot.usecases.user.GetUserFromLocalUseCase
import com.example.campusinteligenteiot.usecases.user.SaveProductLikesUseCase
import com.example.campusinteligenteiot.usecases.user.SaveUserUseCase
import com.example.campusinteligenteiot.usecases.user.SearchUserUseCase
import retrofit2.Response

class ProductDetailViewModel : ViewModel() {
    val getSingleProductUseCase = GetSingleProductUseCase()
    val getUserFromLocalUseCase = GetUserFromLocalUseCase()
    val saveUserUseCase = SaveUserUseCase()
    val saveProductLikesUseCase = SaveProductLikesUseCase()

    suspend fun getSingleProduct(id: String): ProductResponse? {
        return getSingleProductUseCase(id)
    }

    fun getSingleUser(id: String): UsersResponse? {
        return getUserFromLocalUseCase(id)
    }

    suspend fun saveUser(user: UsersResponse, id:String) : Response<String> {
        return saveUserUseCase(id, user)
    }

    suspend fun saveProductLikes(idProducts: ArrayList<String>, id: String){
        saveProductLikesUseCase(idProducts, id)
    }
}