package com.ejabi.yourcart.ui.MAIN

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejabi.domain.model.Category
import com.ejabi.domain.model.Product
import com.ejabi.domain.usecase.Products.GetProductsByCategoryUseCase
import com.ejabi.domain.usecase.Products.GetProductsUseCase
import com.ejabi.domain.usecase.Products.getCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    private val getCategoriesUseCase: getCategoriesUseCase
) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products
    private val _categories = MutableLiveData<List<Category>>()
val categories: LiveData<List<Category>> = _categories
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

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
                _error.value = e.message ?: "Failed to load category products"
            } finally {
                _loading.value = false
            }
        }
    }
}