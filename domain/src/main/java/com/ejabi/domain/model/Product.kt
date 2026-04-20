package com.ejabi.domain.model


data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val thumbnail: String,
    val images: List<String>,
    val rating: Double
)