package com.yahorhous.bkctest.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yahorhous.bkctest.domain.model.product.Product
import com.yahorhous.bkctest.presentation.auth.AuthScreen
import com.yahorhous.bkctest.presentation.cart.CartScreen
import com.yahorhous.bkctest.presentation.cart.CartViewModel
import com.yahorhous.bkctest.presentation.products.ProductListScreen
import com.yahorhous.bkctest.presentation.products.ProductViewModel
import com.yahorhous.bkctest.presentation.products.QuantityDialog
import com.yahorhous.bkctest.presentation.profile.ProfileScreen
import com.yahorhous.bkctest.presentation.profile.ProfileViewModel
import com.yahorhous.bkctest.presentation.profile.PurchaseDetailScreen

@Composable
fun NavigationGraph(
    productViewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(navController)
        }
        composable("cart") {
            CartScreen(navController,
                cartViewModel)
        }
        composable("products") {
            var selectedProduct by remember { mutableStateOf<Product?>(null) }

            Box {
                Log.d("ProductViewModel", "Loaded products")
                ProductListScreen(
                    viewModel = productViewModel,
                    onItemClick = { product ->
                        selectedProduct = product
                    },
                    navController = navController,
                )

                selectedProduct?.let { product ->
                    QuantityDialog(
                        product = product,
                        onDismiss = { selectedProduct = null },
                        onConfirm = { qty ->
                            cartViewModel.addToCart(product, qty)
                            Log.d("QuantityDialog", "Added ${product}")
                            selectedProduct = null
                        }
                    )
                }
            }
        }

        composable("profile") {
            ProfileScreen(navController,
                viewModel = profileViewModel)
        }

        composable(
            route = "purchaseDetails/{timestamp}",
            arguments = listOf(navArgument("timestamp") { type = NavType.LongType })
        ) { backStackEntry ->
            PurchaseDetailScreen(
                timestamp = backStackEntry.arguments?.getLong("timestamp"),
                viewModel = profileViewModel
            )
        }

    }
}