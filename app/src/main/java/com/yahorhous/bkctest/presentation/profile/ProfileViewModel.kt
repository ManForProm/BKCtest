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
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                if (user != null) {
                    loadUserData(user.uid)
                    loadPurchases(user.uid)
                } else {
                    _userName.value = Resource.Error("No user logged in")
                    _purchases.value = Resource.Error("No user logged in")
                }
            }
        }
    }

    private fun loadUserData(userId: String) {
        viewModelScope.launch {
            authRepository.getUserData(userId).collect { resource ->
                _userName.value = resource
            }
        }
    }

    private fun loadPurchases(userId: String) {
        viewModelScope.launch {
            authRepository.getPurchaseHistory(userId).collect { resource ->
                _purchases.value = resource
            }
        }
    }
    fun logout(){
        authRepository.logout()
    }
}