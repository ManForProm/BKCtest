package com.yahorhous.bkctest.presentation.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yahorhous.bkctest.data.repository.ProductRepository
import com.yahorhous.bkctest.domain.model.product.Product
import com.yahorhous.bkctest.domain.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _products = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val products: StateFlow<Resource<List<Product>>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                repository.getAllProducts().collect { products ->
                    _products.value = Resource.Success(products)
                    Log.d("ProductViewModel", "Loaded ${products.size} products")
                }
            } catch (e: Exception) {
                _products.value = Resource.Error(e.message ?: "Unknown error")
                Log.e("ProductViewModel", "Error loading products", e)
            }
        }
    }
}