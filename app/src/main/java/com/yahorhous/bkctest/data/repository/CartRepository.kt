package com.yahorhous.bkctest.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.yahorhous.bkctest.data.local.dao.BarcodeDao
import com.yahorhous.bkctest.domain.model.Resource
import com.yahorhous.bkctest.domain.model.cart.CartItem
import com.yahorhous.bkctest.domain.model.product.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface CartRepository {
    val cartItems: StateFlow<List<CartItem>>
    suspend fun addToCart(product: Product, quantity: Double)
    suspend fun removeFromCart(productId: Int)
    suspend fun clearCart()
    suspend fun checkout(userId: String): Resource<Unit>
}

class CartRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val barcodeDao: BarcodeDao
) : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    override val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    override suspend fun addToCart(product: Product, quantity: Double) {
        Log.d("CartRepo", "Adding product: ${product.id}, price: ${product.price}, bonus: ${product.bonus}")
        if (quantity <= 0) throw IllegalArgumentException("Invalid quantity")
        val barcode = barcodeDao.getBarcodesForPack(product.id).first().firstOrNull()?.body ?: ""
        val existing = _cartItems.value.find { it.product.id == product.id }

        val updatedList = if (existing != null) {
            _cartItems.value.map {
                if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity)
                else it
            }
        } else {
            _cartItems.value + CartItem(product, quantity, barcode)
        }
        _cartItems.emit(updatedList)
        Log.d("CartRepository", "Current _cartItems value: ${_cartItems.value}")
    }

    override suspend fun removeFromCart(productId: Int) {
        _cartItems.emit(_cartItems.value.filter { it.product.id != productId })
    }

    override suspend fun clearCart() {
        _cartItems.emit(emptyList())
    }

    override suspend fun checkout(userId: String): Resource<Unit> {
        if (_cartItems.value.isEmpty()) {
            return Resource.Error("Cart is empty")
        }
        return try {
            val purchase = createPurchase(userId)
            firestore.collection("purchases")
                .add(purchase)
                .addOnSuccessListener { documentReference ->
                    Log.d("CartRepository checkout", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("CartRepository checkout", "Error adding document", e)
                }
                .await()
            clearCart()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Checkout failed")
        }
    }

    private fun createPurchase(userId: String): Map<String, Any> {
        val items = _cartItems.value.map {
            mapOf(
                "name" to it.product.name,
                "quantity" to it.quantity,
                "price" to it.product.finalPrice(),
                "barcode" to it.barcode,
                "unit" to it.product.unit
            )
        }

        return mapOf(
            "userId" to userId,
            "timestamp" to System.currentTimeMillis(),
            "items" to items,
            "total" to _cartItems.value.sumOf { it.totalPrice() }
        )
    }
}
