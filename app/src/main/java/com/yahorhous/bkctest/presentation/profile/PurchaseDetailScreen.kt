package com.yahorhous.bkctest.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yahorhous.bkctest.domain.model.Resource
import com.yahorhous.bkctest.domain.model.cart.PurchaseItem
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PurchaseDetailScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    timestamp: Long?
) {
    val purchase by remember(timestamp) {
        derivedStateOf {
            (viewModel.purchases.value as? Resource.Success)?.data?.find {
                it.timestamp == timestamp
            }
        }
    }

    val currentPurchases = purchase
    if (currentPurchases == null) {
        Text("Покупка не найдена")
        return
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Дата: ${SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(currentPurchases.timestamp))}",
            style = MaterialTheme.typography.titleLarge
        )
        Text("Общая сумма: ${currentPurchases.total / 100.0} руб")

        LazyColumn {
            items(currentPurchases.items) { item ->
                PurchaseItemDetail(item = item)
            }
        }
    }
}

@Composable
fun PurchaseItemDetail(item: PurchaseItem) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.productName, style = MaterialTheme.typography.titleMedium)
            Text("Количество: ${item.quantity} ${item.unit}")
            Text("Цена: ${item.price / 100.0} руб")
            Text("Штрихкод: ${item.barcode}")
        }
    }
}