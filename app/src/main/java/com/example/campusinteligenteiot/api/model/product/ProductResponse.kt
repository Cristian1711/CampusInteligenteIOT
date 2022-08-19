package com.example.campusinteligenteiot.api.model.product

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id") var id: String,
    @SerializedName("title") var title: String,
    @SerializedName("description") var description: String,
    @SerializedName("price") var price: Float,
    @SerializedName("idOwner") var idOwner: String,
    @SerializedName("productImage") var productImage: String?,
    @SerializedName("published") var published: Boolean,
    @SerializedName("category") var category: String,
    @SerializedName("archived") var archived: Boolean
)