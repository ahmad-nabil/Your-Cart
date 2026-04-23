package com.ejabi.domain.usecase.cart

import com.ejabi.domain.model.CartItem
import com.ejabi.domain.model.Product
import com.ejabi.domain.repository.CartRepository


class AddToCartUseCase (
    private val cartRepository: CartRepository
) {
    /**
     * sealed class Result wraps success/failure.
     * WHY sealed class instead of throwing exceptions?
     * - Exceptions are invisible in the function signature — callers don't know what can go wrong
     * - Result makes ALL outcomes explicit: ViewModel must handle Success AND Error
     * - Safer, more readable, no try/catch clutter in ViewModel
     */
    sealed class Result {
        data class Success(val item: CartItem) : Result()
        data class Error(val message: String) : Result()
    }

    suspend operator fun invoke(product: Product, quantity: Int = 1): Result {
        // --- Business rule 1: price must be valid ---
        if (product.price <= 0) {
            return Result.Error("Product has an invalid price")
        }

        // --- Business rule 2: title must not be blank ---
        if (product.title.isBlank()) {
            return Result.Error("Product is missing a title")
        }

        // --- Business rule 3: quantity cap ---
        val safeQuantity = quantity.coerceIn(1, 99)

        // --- Mapping: Product (API model) → CartItem (domain model) ---
        // This mapping is a BUSINESS decision (which fields matter for the cart)
        // It belongs here, not in ViewModel or Repository
        val cartItem = CartItem(
            productId = product.id,
            title = product.title,
            price = product.price,
            imageUrl = product.images.firstOrNull() ?: "",
            category = product.category,
            quantity = safeQuantity
        )

        // --- Delegate the actual storage to the repository ---
        cartRepository.addItem(cartItem)

        return Result.Success(cartItem)
    }
}
