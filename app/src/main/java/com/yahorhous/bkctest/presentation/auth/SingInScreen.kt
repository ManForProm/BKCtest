package com.yahorhous.bkctest.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yahorhous.bkctest.domain.model.Resource
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignInScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSuccess: () -> Unit,
    onSwitchToSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                if (validateInput(email, password)) {
                    viewModel.signIn(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Button(
            onClick = onSwitchToSignUp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Switch to Sign Up")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.signInState.collectLatest { result ->
            when (result) {
                is Resource.Success<*> -> onSuccess()
                is Resource.Error<*> -> errorMessage = result.message
                is Resource.Loading<*> -> errorMessage = null
                null -> {}
            }
        }
    }
}

private fun validateInput(email: String, password: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password.length >= 6
}