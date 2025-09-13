package com.example.simple_note_test.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(val username: String, val password: String)
@JsonClass(generateAdapter = true)
data class RegisterRequest(val first_name: String, val last_name: String, val username: String, val email: String, val password: String)
@JsonClass(generateAdapter = true)
data class ForgotPasswordRequest(val email: String)
@JsonClass(generateAdapter = true)
data class ResetPasswordRequest(val token: String, val new_password: String)
@JsonClass(generateAdapter = true)
data class ChangePasswordRequest(val old_password: String, val new_password: String)
@JsonClass(generateAdapter = true)
data class RefreshTokenRequest(val refresh: String)
@JsonClass(generateAdapter = true)
data class AuthResponse(val access: String, val refresh: String)
@JsonClass(generateAdapter = true)
data class BaseResponse(val detail: String?)
@JsonClass(generateAdapter = true)
data class UserProfileResponse(val id: String, val email: String, val username: String, val first_name: String, val last_name: String, val avatar: String?)
