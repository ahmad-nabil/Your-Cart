package com.ejabi.yourcart.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejabi.domain.model.CartItem
import com.ejabi.domain.model.Category
import com.ejabi.domain.model.Product
import com.ejabi.domain.usecase.Products.GetProductsByCategoryUseCase
import com.ejabi.domain.usecase.Products.GetProductsUseCase
import com.ejabi.domain.usecase.Products.getCategoriesUseCase
import com.ejabi.domain.usecase.cart.AddToCartUseCase
import com.ejabi.domain.usecase.cart.ClearCartUseCase
import com.ejabi.domain.usecase.cart.GetCartItemsUseCase
import com.ejabi.domain.usecase.cart.RemoveFromCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    // --- Product use cases (your existing ones) ---
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val getCategoriesUseCase: getCategoriesUseCase,
    // --- Cart use cases (new) ---
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : ViewModel() {

    // -------------------------------------------------------------------------
    // PRODUCTS STATE
    // -------------------------------------------------------------------------

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // -------------------------------------------------------------------------
    // CART STATE
    // -------------------------------------------------------------------------

    /**
     * cartItems — live list of everything saved in Room.
     *
     * getCartItemsUseCase() returns a Flow<List<CartItem>> from the repository.
     * .stateIn() converts it into a StateFlow so the UI can collect it.
     *
     * WHY StateFlow and not LiveData?
     * - Cart data comes from Room as a Flow (reactive stream)
     * - StateFlow is the coroutines-native way to hold that stream as state
     * - It always has a current value (initialValue = emptyList)
     * - Safe to collect in Activity/Fragment using lifecycleScope
     *
     * SharingStarted.WhileSubscribed(5_000):
     * - Keeps the Flow alive 5 seconds after the last subscriber leaves
     * - Handles screen rotation: Activity is destroyed and recreated within ~1s
     *   so the Flow stays warm and doesn't restart the DB query
     */
    val cartItems: StateFlow<List<CartItem>> = getCartItemsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /**
     * cartEvent — one-shot events sent to the UI (show toast, show snackbar).
     *
     * WHY separate from cartItems?
     * - cartItems is PERSISTENT STATE: the list should survive rotation
     * - cartEvent is a ONE-SHOT EVENT: "Added to cart!" toast should show ONCE,
     *   not replay every time the screen recomposes or rotates
     *
     * Pattern:
     * 1. ViewModel sets _cartEvent.value = CartEvent.AddedToCart(...)
     * 2. UI collects it, shows the toast
     * 3. UI calls onCartEventConsumed() → resets to null
     * 4. No duplicate toasts on rotation
     */
    private val _cartEvent = MutableStateFlow<CartEvent?>(null)
    val cartEvent: StateFlow<CartEvent?> = _cartEvent.asStateFlow()

    sealed class CartEvent {
        data class AddedToCart(val productName: String) : CartEvent()
        data class Error(val message: String) : CartEvent()
    }

    // -------------------------------------------------------------------------
    // PRODUCT ACTIONS (your existing functions — unchanged)
    // -------------------------------------------------------------------------

    fun loadProducts() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _products.value = getProductsUseCase()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load products"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _products.value = getProductsByCategoryUseCase(category)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load category products"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadCategory() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _categories.value = getCategoriesUseCase()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load categories"
            } finally {
                _loading.value = false
            }
        }
    }


    fun addToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            when (val result = addToCartUseCase(product, quantity)) {
                is AddToCartUseCase.Result.Success -> {
                    _cartEvent.value = CartEvent.AddedToCart(result.item.title)
                }
                is AddToCartUseCase.Result.Error -> {
                    _cartEvent.value = CartEvent.Error(result.message)
                }
            }
        }
    }


    fun removeFromCart(item: CartItem) {
        viewModelScope.launch {
            removeFromCartUseCase(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase()
        }
    }


    fun onCartEventConsumed() {
        _cartEvent.value = null
    }
}