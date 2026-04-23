package com.ejabi.domain.model

data class CartItem(
    val id: Int = 0,
    val productId: Int,
    val title: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val quantity: Int = 1
)