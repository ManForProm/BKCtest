package com.yahorhous.bkctest.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yahorhous.bkctest.data.repository.AuthRepository
import com.yahorhous.bkctest.domain.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val authRepository: AuthRepository
) : ViewModel() {
    private val _signUpState = MutableStateFlow<Resource<Unit>?>(null)
    val signUpState: StateFlow<Resource<Unit>?> = _signUpState

    private val _signInState = MutableStateFlow<Resource<Unit>?>(null)
    val signInState: StateFlow<Resource<Unit>?> = _signInState

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signUpState.value = Resource.Loading()
            _signUpState.value = authRepository.signUp(name, email, password)
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInState.value = Resource.Loading()
            _signInState.value = authRepository.signIn(email, password)
        }
    }
}