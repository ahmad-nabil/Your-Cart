package com.ejabi.domain.usecase.cart

import com.ejabi.domain.model.CartItem
import com.ejabi.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
class GetCartItemsUseCase (
    private val cartRepository: CartRepository
) {
    /**
     * operator fun invoke() with no params returns a Flow.
     * The ViewModel collects this Flow the same way as before —
     * nothing changes in the UI layer, only WHERE the logic lives changes.
     */
    operator fun invoke(): Flow<List<CartItem>> {
        return cartRepository.getAllItems()
            .map { items ->
                // Business rule: sort alphabetically by title for consistent display
                items.sortedBy { it.title }
                // You could also filter, transform, or enrich here
            }
    }
}
