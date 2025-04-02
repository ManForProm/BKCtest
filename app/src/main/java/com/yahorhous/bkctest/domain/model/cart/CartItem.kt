package com.yahorhous.bkctest.domain.model.cart

import android.util.Log
import com.yahorhous.bkctest.domain.model.product.Product

data class CartItem(
    val product: Product,
    val quantity: Double,
    val barcode: String
) {
    fun totalPrice():Int {
        val total = (product.finalPrice() * quantity).toInt()
        Log.d("CartItem", "Calculated total: $total for ${product.name}")
        return total
    }
}