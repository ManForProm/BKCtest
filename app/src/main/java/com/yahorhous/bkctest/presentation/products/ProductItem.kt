package com.yahorhous.bkctest.presentation.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.yahorhous.bkctest.domain.model.product.Product

@Composable
fun ProductItem(
    product: Product,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onItemClick,
        modifier = modifier.padding(8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Цены
            val (unitText, originalPrice, finalPrice) = calculatePrices(product)

            if (product.bonus > 0) {
                Text(
                    text = "Цена: ${String.format("%.2f", originalPrice)} руб/$unitText",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.LineThrough
                    ),
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Text(
                text = "Цена со скидкой: ${String.format("%.2f", finalPrice)} руб/$unitText",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            if (product.bonus > 0) {
                Text(
                    text = "Скидка: ${product.bonus / 100} руб",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun calculatePrices(product: Product): Triple<String, Double, Double> {
    return when (product.type) {
        0 -> Triple(
            "шт",
            product.price.toDouble() / 100,
            product.finalPrice().toDouble() / 100
        )
        1 -> Triple(
            "кг",
            product.price.toDouble() / 100, // цена за 1кг
            product.finalPrice().toDouble() / 100
        )
        else -> Triple("", 0.0, 0.0)
    }
}
