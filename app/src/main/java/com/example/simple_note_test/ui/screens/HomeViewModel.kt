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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val tokenStorage: TokenStorage
) : ViewModel() {
    private val _notesState = MutableStateFlow<NetworkResult<List<NoteResponse>>>(NetworkResult.Loading)
    val notesState: StateFlow<NetworkResult<List<NoteResponse>>> = _notesState

    // pagination state
    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    private var aggregatedNotes: MutableList<NoteResponse> = mutableListOf()
    private var nextPage: Int? = null
    private var currentSearch: String? = null
    private var isInitialLoadDone = false

    init {
        val token = tokenStorage.getAccessToken()
        if (!token.isNullOrEmpty()) {
            loadNotes()
        } else {
            _notesState.value = NetworkResult.Error("No access token found. Please login again.")
        }
    }

    // Helper to extract 'page' query param from next/previous URL if present
    private fun extractPageFromUrl(url: String?): Int? {
        if (url == null) return null
        // simple regex to find page=NUMBER
        val regex = "[?&]page=(\\d+)".toRegex()
        val match = regex.find(url)
        return match?.groups?.get(1)?.value?.toIntOrNull()
    }

    fun loadNotes(search: String? = null, page: Int? = 1, append: Boolean = false) {
        // reset aggregator if not appending or search changed
        if (!append || currentSearch != search) {
            aggregatedNotes = mutableListOf()
        }
        currentSearch = search

        viewModelScope.launch {
            try {
                if (!append) _notesState.value = NetworkResult.Loading
                _isLoadingMore.value = append

                val response = notesRepository.getNotes(search, page)
                // response contains results and pagination fields
                if (append) {
                    aggregatedNotes.addAll(response.results)
                } else {
                    aggregatedNotes = response.results.toMutableList()
                }

                // determine next page
                nextPage = extractPageFromUrl(response.next)

                _notesState.value = NetworkResult.Success(aggregatedNotes.toList())
            } catch (e: Exception) {
                _notesState.value = NetworkResult.Error(e.message ?: "Unknown error")
            } finally {
                _isLoadingMore.value = false
                isInitialLoadDone = true
            }
        }
    }

    fun loadNextPage() {
        // prevent duplicate loads, only load if nextPage exists
        if (_isLoadingMore.value) return
        if (!isInitialLoadDone) return
        val pageToLoad = nextPage ?: return
        loadNotes(currentSearch, pageToLoad, append = true)
    }
}
