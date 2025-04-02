package com.yahorhous.bkctest.domain.model.cart

data class Purchase(
    val userId: String,
    val timestamp: Long,
    val items: List<PurchaseItem>,
    val total: Int
)