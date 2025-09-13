package com.example.simple_note_test.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simple_note_test.data.local.TokenStorage
import com.example.simple_note_test.data.models.NoteResponse
import com.example.simple_note_test.data.repos.NotesRepository
import com.example.simple_note_test.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val tokenStorage: TokenStorage
) : ViewModel() {
    private val _notesState = MutableStateFlow<NetworkResult<List<NoteResponse>>>(NetworkResult.Loading)
    val notesState: StateFlow<NetworkResult<List<NoteResponse>>> = _notesState

    init {
        val token = tokenStorage.getAccessToken()
        if (!token.isNullOrEmpty()) {
            loadNotes()
        } else {
            _notesState.value = NetworkResult.Error("No access token found. Please login again.")
        }
    }

    fun loadNotes() {
        viewModelScope.launch {
            _notesState.value = NetworkResult.Loading
            try {
                val response = notesRepository.getNotes()
                _notesState.value = NetworkResult.Success(response.results)
            } catch (e: Exception) {
                _notesState.value = NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}
