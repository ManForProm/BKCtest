package com.yahorhous.bkctest.presentation.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yahorhous.bkctest.data.repository.AuthRepository
import com.yahorhous.bkctest.data.repository.CartRepository
import com.yahorhous.bkctest.domain.model.Resource
import com.yahorhous.bkctest.domain.model.cart.CartItem
import com.yahorhous.bkctest.domain.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    ).also {
        Log.d("CartViewModel", "Cart items initialized")
    }

    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        val total = items.sumOf { it.totalPrice() } / 100.0
        Log.d("CartViewModel", "Calculated total price: $total from items: $items")
        total
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    fun addToCart(product: Product, quantity: Double) = viewModelScope.launch {
        repository.addToCart(product, quantity)
        Log.d("addToCart", "Added ${product}")
    }

    fun removeFromCart(productId: Int) = viewModelScope.launch {
        Log.d("CartViewModel", "Removing product with ID: $productId")
        repository.removeFromCart(productId)
    }

    fun checkout(onSuccess: () -> Unit) = viewModelScope.launch {
        val user = authRepository.getCurrentUser().first() ?: return@launch
        Log.d("CartViewModel", "Current User: $user")
        when (val result = repository.checkout(user.uid)) {
            is Resource.Success -> onSuccess()
            is Resource.Error -> {/* Handle error */}
            else -> {}
        }
    }
}