package com.example.simple_note_test.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simple_note_test.data.local.TokenStorage
import com.example.simple_note_test.data.models.LoginRequest
import com.example.simple_note_test.data.repos.AuthRepository
import com.example.simple_note_test.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenStorage: TokenStorage
) : ViewModel() {
    init {
        Log.d("LoginVM", "created")
    }

    // internal backing flow (renamed)
    private val _stateFlow = MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle)
    val loginState: StateFlow<NetworkResult<Boolean>> = _stateFlow

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _stateFlow.value = NetworkResult.Error("Username and password must not be empty")
            return
        }

        viewModelScope.launch {
            _stateFlow.value = NetworkResult.Loading
            kotlin.runCatching {
                authRepository.login(LoginRequest(username, password))
            }.onSuccess { response ->
                tokenStorage.saveTokens(response.access, response.refresh)
                _stateFlow.value = NetworkResult.Success(true)
            }.onFailure { err ->
                _stateFlow.value = NetworkResult.Error(err.message ?: "Unknown error")
            }
        }
    }
}
