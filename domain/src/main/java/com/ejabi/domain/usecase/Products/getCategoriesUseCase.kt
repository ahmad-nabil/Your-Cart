package com.ejabi.domain.usecase.Products

import com.ejabi.domain.model.Category
import com.ejabi.domain.repository.ProductRepository

class getCategoriesUseCase (
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): List<Category> {
        return repository.getCategories()
    }
}