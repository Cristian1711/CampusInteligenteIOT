package com.example.campusinteligenteiot.model

data class Product(val idProduct:String,
                   val title: String? = null,
                   val description: String? = null,
                   val price: Float? = null,
                   val idOwner: String? = null,
                   val productImage: String? = null
                    )