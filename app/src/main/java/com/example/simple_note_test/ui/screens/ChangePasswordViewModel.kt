package com.example.simple_note_test.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simple_note_test.data.models.ChangePasswordRequest
import com.example.simple_note_test.data.repos.AuthRepository
import com.example.simple_note_test.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _changeState = MutableStateFlow<NetworkResult<Unit>>(NetworkResult.Idle)
    val changeState: StateFlow<NetworkResult<Unit>> = _changeState

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _changeState.value = NetworkResult.Loading
            try {
                authRepository.changePassword(ChangePasswordRequest(oldPassword, newPassword))
                _changeState.value = NetworkResult.Success(Unit)
            } catch (e: Exception) {
                _changeState.value = NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}
