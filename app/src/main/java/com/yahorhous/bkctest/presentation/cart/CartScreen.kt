package com.yahorhous.bkctest.presentation.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yahorhous.bkctest.domain.model.cart.CartItem
import com.yahorhous.bkctest.domain.model.product.Product

@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel,
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        if (cartItems.isEmpty()) {
            Text("Корзина пуста", modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item ->
                    CartItemRow(
                        item = item,
                        onRemove = { viewModel.removeFromCart(item.product.id) }
                    )
                }
            }

            Text(
                text = "Итого: ${String.format("%.2f", totalPrice)} руб",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Button(
                onClick = {
                    viewModel.checkout {
                        navController.navigate("products") {
                            popUpTo("cart") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Оплатить")
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    Card(modifier = Modifier.padding(vertical = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(item.product.name)
                Text("${item.quantity} ${getUnitDisplay(item.product)}")
                Text("${String.format("%.2f", item.totalPrice() / 100.0)} руб")
                Text("Штрихкод: ${item.barcode}")
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, "Удалить")
            }
        }
    }
}

private fun getUnitDisplay(product: Product): String = when (product.type) {
    0 -> "шт"
    1 -> "кг"
    else -> ""
}