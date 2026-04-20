package com.ejabi.domain.usecase.Products

import com.ejabi.domain.model.Product
import com.ejabi.domain.repository.ProductRepository

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): List<Product> {
        return repository.getProducts()
    }
}