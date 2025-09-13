package com.example.simple_note_test.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simple_note_test.data.local.TokenStorage
import com.example.simple_note_test.data.models.RegisterRequest
import com.example.simple_note_test.data.repos.AuthRepository
import com.example.simple_note_test.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _registerState =
        MutableStateFlow<NetworkResult<Boolean>>(NetworkResult.Idle)
    val registerState: StateFlow<NetworkResult<Boolean>> = _registerState

    fun register(request: RegisterRequest) {
        // Input validation
        if (request.first_name.isBlank() || request.last_name.isBlank() || request.username.isBlank() ||
            request.email.isBlank() || request.password.isBlank()) {
            _registerState.value = NetworkResult.Error("All fields must be filled")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(request.email).matches()) {
            _registerState.value = NetworkResult.Error("Invalid email address")
            return
        }
        if (request.password.length < 6) {
            _registerState.value = NetworkResult.Error("Password must be at least 6 characters")
            return
        }
        viewModelScope.launch {
            _registerState.value = NetworkResult.Loading
            try {
                // Register user (returns RegisterResponse, not tokens)
                authRepository.register(request)
                _registerState.value = NetworkResult.Success(true)
            } catch (e: Exception) {
                _registerState.value = NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}
