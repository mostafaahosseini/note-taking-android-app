package com.example.simple_note_test.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NoteResponse(
    val id: String,
    val title: String,
    val description: String,
    val updated_at: String
)
@JsonClass(generateAdapter = true)
data class NotesListResponse(val results: List<NoteResponse>)
@JsonClass(generateAdapter = true)
data class NoteCreateRequest(val title: String, val description: String)
@JsonClass(generateAdapter = true)
data class NoteUpdateRequest(val title: String, val description: String)
