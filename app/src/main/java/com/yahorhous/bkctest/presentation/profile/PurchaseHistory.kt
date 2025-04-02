package com.yahorhous.bkctest.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yahorhous.bkctest.domain.model.cart.Purchase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PurchaseHistory(
    purchases: List<Purchase>,
    onItemClick: (Purchase) -> Unit
) {
    LazyColumn {
        items(purchases) { purchase ->
            PurchaseItem(purchase, onItemClick)
        }
    }
}

@Composable
fun PurchaseItem(purchase: Purchase, onClick: (Purchase) -> Unit) {
    Card(
        onClick = { onClick(purchase) },
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Дата: ${Date(purchase.timestamp).formatToString()}")
            Text("Общая сумма: ${purchase.total / 100.0} руб")
        }
    }
}

fun Date.formatToString(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(this)
}