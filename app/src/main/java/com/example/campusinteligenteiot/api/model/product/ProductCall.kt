package com.example.campusinteligenteiot.api.model.product

class ProductCall (
    var title: String? = null,
    var description: String? = null,
    var price: Float? = null,
    var idOwner: String? = null,
    var productImage: String? = null,
    var published: Boolean,
    var category: String,
    var archived: Boolean
)