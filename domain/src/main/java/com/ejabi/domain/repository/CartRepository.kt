package com.ejabi.domain.repository

import com.ejabi.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getAllItems(): Flow<List<CartItem>>

    suspend fun addItem(item: CartItem)

    suspend fun removeItem(item: CartItem)

    suspend fun updateQuantity(productId: Int, quantity: Int)

    suspend fun clearCart()
}