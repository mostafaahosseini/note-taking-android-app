package com.example.simple_note_test.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simple_note_test.data.models.NoteCreateRequest
import com.example.simple_note_test.data.models.NoteUpdateRequest
import com.example.simple_note_test.data.repos.NotesRepository
import com.example.simple_note_test.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(private val notesRepository: NotesRepository) : ViewModel() {
    private val _editState = MutableStateFlow<NetworkResult<Unit>>(NetworkResult.Idle)
    val editState: StateFlow<NetworkResult<Unit>> = _editState

    private val _noteState = MutableStateFlow<NetworkResult<com.example.simple_note_test.data.models.NoteResponse>>(NetworkResult.Idle)
    val noteState: StateFlow<NetworkResult<com.example.simple_note_test.data.models.NoteResponse>> = _noteState

    fun loadNote(id: String) {
        viewModelScope.launch {
            _noteState.value = NetworkResult.Loading
            try {
                val note = notesRepository.getNote(id)
                _noteState.value = NetworkResult.Success(note)
            } catch (e: Exception) {
                _noteState.value = NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createNote(title: String, description: String) {
        viewModelScope.launch {
            _editState.value = NetworkResult.Loading
            try {
                notesRepository.createNote(NoteCreateRequest(title, description))
                _editState.value = NetworkResult.Success(Unit)
            } catch (e: Exception) {
                _editState.value = NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateNote(id: String, title: String, description: String) {
        viewModelScope.launch {
            _editState.value = NetworkResult.Loading
            try {
                notesRepository.updateNote(id, NoteUpdateRequest(title, description))
                _editState.value = NetworkResult.Success(Unit)
            } catch (e: Exception) {
                _editState.value = NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            _editState.value = NetworkResult.Loading
            try {
                val response = notesRepository.deleteNote(id)
                if (response.isSuccessful) {
                    _editState.value = NetworkResult.Success(Unit)
                } else {
                    _editState.value = NetworkResult.Error("Failed to delete note. Server returned ${response.code()}")
                }
            } catch (e: Exception) {
                _editState.value = NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}
