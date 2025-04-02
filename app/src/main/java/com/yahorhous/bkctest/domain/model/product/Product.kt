package com.yahorhous.bkctest.domain.model.product

data class Product(
    val id: Int,
    val name: String,
    val type: Int,
    val unit: String,
    val price: Int,
    val bonus: Int,
    val quant: Int
) {
    fun finalPrice() = (price - bonus)

    fun pricePerKg(): Double {
        return if (type == 1) {
            (finalPrice() / 100.0) * (1000.0 / quant)
        } else {
            finalPrice() / 100.0
        }
    }

    fun originalPricePerKg(): Double {
        return if (type == 1) {
            (price / 100.0) * (1000.0 / quant)
        } else {
            price / 100.0
        }
    }

}