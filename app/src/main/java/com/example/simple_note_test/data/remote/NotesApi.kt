package com.example.simple_note_test.data.remote

import com.example.simple_note_test.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface NotesApi {
    @GET("api/notes/")
    suspend fun getNotes(@Query("search") search: String? = null): NotesListResponse

    @GET("api/notes/{id}/")
    suspend fun getNote(@Path("id") id: String): NoteResponse

    @POST("api/notes/")
    suspend fun createNote(@Body request: NoteCreateRequest): NoteResponse

    @PUT("api/notes/{id}/")
    suspend fun updateNote(@Path("id") id: String, @Body request: NoteUpdateRequest): NoteResponse

    @DELETE("api/notes/{id}/")
    suspend fun deleteNote(@Path("id") id: String): Response<Unit>
}
