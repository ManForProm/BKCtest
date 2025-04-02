package com.yahorhous.bkctest.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yahorhous.bkctest.domain.model.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()
    val purchases by viewModel.purchases.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        navController.navigate("auth") { popUpTo(0) }
                    }) {
                        Icon(Icons.Default.Logout, "Выход")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val currentUserName = userName
            when (currentUserName) {
                is Resource.Success -> {
                    Text(
                        "Пользователь: ${currentUserName.data}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is Resource.Error -> {
                    Text("Ошибка загрузки данных")
                }
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
            }
            val currentPurchases = purchases
            when (currentPurchases) {
                is Resource.Success -> {
                    PurchaseHistory(
                        purchases = currentPurchases.data,
                        onItemClick = { purchase ->
                            navController.navigate("purchaseDetails/${purchase.timestamp}")
                        }
                    )
                }
                is Resource.Error -> {
                    Text("Ошибка загрузки истории")
                }
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}