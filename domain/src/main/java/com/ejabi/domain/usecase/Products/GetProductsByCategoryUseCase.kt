package com.ejabi.domain.usecase.Products



import com.ejabi.domain.model.Product
import com.ejabi.domain.repository.ProductRepository


class GetProductsByCategoryUseCase (
    private val repository: ProductRepository
) {
    suspend operator fun invoke(category: String): List<Product> {
        return repository.getProductsByCategory(category)
    }
}