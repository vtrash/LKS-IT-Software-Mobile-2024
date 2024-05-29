package com.example.smkn1cianjur_06_satriaanandabintang.model

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String,
    val rating: Double,
    var quantity: Int = 0,
    var subtotal: Int = 0,
)
