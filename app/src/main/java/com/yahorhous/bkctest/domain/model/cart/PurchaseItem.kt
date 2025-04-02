package com.yahorhous.bkctest.domain.model.cart

data class PurchaseItem(
    val productName: String,
    val quantity: Double,
    val price: Int,
    val barcode: String,
    val unit: String
)