package com.ejabi.domain.repository

import com.ejabi.domain.model.Category
import com.ejabi.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getCategories(): List<Category>
    suspend fun getProductsByCategory(category: String): List<Product>

}