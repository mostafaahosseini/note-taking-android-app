package com.example.simple_note_test.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simple_note_test.data.models.NoteResponse
import com.example.simple_note_test.data.models.NoteUpdateRequest
import com.example.simple_note_test.data.repos.NotesRepository
import com.example.simple_note_test.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(private val notesRepository: NotesRepository) : ViewModel() {
    private val _noteState = MutableStateFlow<NetworkResult<Unit>>(NetworkResult.Loading)
    val noteState: StateFlow<NetworkResult<Unit>> = _noteState

    private val _note = MutableStateFlow<NoteResponse?>(null)
    val note: StateFlow<NoteResponse?> = _note
    private val _editState = MutableStateFlow<NetworkResult<Unit>>(NetworkResult.Loading)
    val editState: StateFlow<NetworkResult<Unit>> = _editState

    fun loadNote(id: String) {
        viewModelScope.launch {
            // Do NOT set _editState here!
            try {
                val response = notesRepository.getNote(id)
                _note.value = response
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
                // Reload note after update
                loadNote(id)
                _editState.value = NetworkResult.Success(Unit)
            } catch (e: Exception) {
                _editState.value = NetworkResult.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteNote(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _editState.value = NetworkResult.Loading
            try {
                notesRepository.deleteNote(id)
                _editState.value = NetworkResult.Success(Unit)
                onSuccess()
            } catch (e: Exception) {
                val msg = e.message ?: ""
                if (msg.contains("was null but response body type was declared as non-Null", ignoreCase = true)) {
                    // Treat this as success (Retrofit empty response bug)
                    _editState.value = NetworkResult.Success(Unit)
                    onSuccess()
                } else {
                    _editState.value = NetworkResult.Error(msg.ifBlank { "Unknown error" })
                }
            }
        }
    }
}
