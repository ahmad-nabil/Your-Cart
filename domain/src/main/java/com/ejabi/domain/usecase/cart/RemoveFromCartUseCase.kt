package com.ejabi.domain.usecase.cart

import com.ejabi.domain.model.CartItem
import com.ejabi.domain.repository.CartRepository

class RemoveFromCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(item: CartItem) {
        if (item.quantity > 1) {
            cartRepository.updateQuantity(item.productId, item.quantity - 1)
        } else {
            cartRepository.removeItem(item)
        }
    }
}
