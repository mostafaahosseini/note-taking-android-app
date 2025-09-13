package com.example.simple_note_test.data.repos

import com.example.simple_note_test.data.models.*
import com.example.simple_note_test.data.remote.NotesApi
import javax.inject.Inject

class NotesRepository @Inject constructor(private val api: NotesApi) {
    suspend fun getNotes(search: String? = null) = api.getNotes(search)
    suspend fun getNote(id: String) = api.getNote(id)
    suspend fun createNote(request: NoteCreateRequest) = api.createNote(request)
    suspend fun updateNote(id: String, request: NoteUpdateRequest) = api.updateNote(id, request)
    suspend fun deleteNote(id: String): retrofit2.Response<Unit> = api.deleteNote(id)
}
