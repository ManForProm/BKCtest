package com.yahorhous.bkctest.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yahorhous.bkctest.data.repository.AuthRepository
import com.yahorhous.bkctest.domain.model.Resource
import com.yahorhous.bkctest.domain.model.cart.Purchase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _userName = MutableStateFlow<Resource<String>>(Resource.Loading())
    val userName: StateFlow<Resource<String>> = _userName

    private val _purchases = MutableStateFlow<Resource<List<Purchase>>>(Resource.Loading())
    val purchases: StateFlow<Resource<List<Purchase>>> = _purchases

    init {
        loadUserData()
        loadPurchases()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.uid?.let { uid ->
                _userName.value = authRepository.getUserData(uid)
            }
        }
    }

    private fun loadPurchases() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.uid?.let { uid ->
                authRepository.getPurchaseHistory(uid).collect { resource ->
                    _purchases.value = resource
                }
            }
        }
    }
    fun logout(){
        authRepository.logout()
    }
}