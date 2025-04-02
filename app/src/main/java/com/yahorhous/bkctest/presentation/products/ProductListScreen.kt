package com.yahorhous.bkctest.presentation.products

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yahorhous.bkctest.domain.model.product.Product
import com.yahorhous.bkctest.domain.model.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    onItemClick: (Product) -> Unit,
    navController: NavController,
) {
    val products by viewModel.products.collectAsState()
    Scaffold( topBar = {
        TopAppBar(
            title = { Text("Товары") },
            actions = {
                IconButton(onClick = { navController.navigate("profile") }) {
                    Icon(Icons.Default.Person, "Профиль")
                }
            }
        )
    },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("cart") }) {
                Icon(Icons.Default.ShoppingCart, "Корзина")
            }
        }
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
            Log.d("ProductViewModel", "Loaded ${products} products")
            when (products) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is Resource.Error -> {
                    Text(
                        text = "Ошибка загрузки товаров",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is Resource.Success -> {
                    val productList = (products as Resource.Success<List<Product>>).data
                    if (productList.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(productList) { product ->
                                ProductItem(
                                    product = product,
                                    onItemClick = { onItemClick(product) }
                                )
                            }
                        }
                    } else {
                        Text("Список пуст")
                    }
                }
            }
        }
    }

}