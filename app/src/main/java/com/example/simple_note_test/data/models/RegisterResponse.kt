package com.example.simple_note_test.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    val username: String,
    val email: String,
    val first_name: String,
    val last_name: String
)
