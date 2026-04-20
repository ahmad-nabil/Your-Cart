package com.ejabi.data.remote.DTo

data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val thumbnail: String,
    val images: List<String>,
    val rating: Double
)