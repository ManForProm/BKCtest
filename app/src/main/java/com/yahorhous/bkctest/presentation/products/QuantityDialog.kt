package com.yahorhous.bkctest.presentation.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.yahorhous.bkctest.domain.model.product.Product

@Composable
fun QuantityDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var quantity by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите количество") },
        text = {
            Column {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text(getLabelForProduct(product)) },
                    isError = error != null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (product.type == 0)
                            KeyboardType.Number
                        else
                            KeyboardType.Decimal
                    )
                )

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = quantity.toDoubleOrNull()
                    when {
                        qty == null -> error = "Некорректное значение"
                        product.type == 0 && qty % 1 != 0.0 ->
                            error = "Введите целое число"
                        qty <= 0 -> error = "Введите значение больше 0"
                        product.type == 1 && qty < 0.1 ->
                            error = "Минимальное количество 0.1 кг"
                        else -> {
                            onConfirm(qty)
                            onDismiss()
                        }
                    }
                }
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}

private fun getLabelForProduct(product: Product): String {
    return when (product.type) {
        0 -> "Количество (шт)"
        1 -> "Вес (кг)"
        else -> ""
    }
}