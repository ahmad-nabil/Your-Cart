package com.ejabi.domain.repository

import com.ejabi.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}