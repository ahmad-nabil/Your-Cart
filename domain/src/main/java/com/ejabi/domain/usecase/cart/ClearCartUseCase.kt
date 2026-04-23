package com.ejabi.domain.usecase.cart

import com.ejabi.domain.repository.CartRepository

class ClearCartUseCase (
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke() {
        cartRepository.clearCart()
    }
}
