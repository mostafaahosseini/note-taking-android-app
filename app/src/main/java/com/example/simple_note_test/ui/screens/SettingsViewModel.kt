package com.example.simple_note_test.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simple_note_test.data.local.TokenStorage
import com.example.simple_note_test.data.models.UserProfileResponse
import com.example.simple_note_test.data.repos.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenStorage: TokenStorage
) : ViewModel() {
    private val _profile = MutableStateFlow<UserProfileResponse?>(null)
    val profile: StateFlow<UserProfileResponse?> = _profile

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            try {
                _profile.value = userRepository.getProfile()
            } catch (e: Exception) {
                _profile.value = null
            }
        }
    }

    // Add settings/profile logic here

    fun logout() {
        tokenStorage.clearTokens()
    }
}
