package com.yahorhous.bkctest.presentation.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var isSignUp by remember { mutableStateOf(false) }

    val currentUser by viewModel.authRepository.getCurrentUser().collectAsState(initial = null)

    if (currentUser != null) {
        LaunchedEffect(Unit) {
            navController.navigate("products") {
                popUpTo("auth") { inclusive = true }
            }
        }
    } else {
        Column(Modifier.padding(16.dp)) {
            if (isSignUp) {
                SignUpScreen(
                    viewModel = viewModel,
                    onSuccess = { navController.navigate("products") },
                    onSwitchToLogin = { isSignUp = false }
                )
            } else {
                SignInScreen(
                    viewModel = viewModel,
                    onSuccess = { navController.navigate("products") },
                    onSwitchToSignUp = { isSignUp = true }
                )
            }
        }
    }
}