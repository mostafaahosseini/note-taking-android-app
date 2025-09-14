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
data class NotesListResponse(
    val results: List<NoteResponse>,
    val next: String? = null,
    val previous: String? = null,
    val count: Int? = null
)
@JsonClass(generateAdapter = true)
data class NoteCreateRequest(val title: String, val description: String)
@JsonClass(generateAdapter = true)
data class NoteUpdateRequest(val title: String, val description: String)
