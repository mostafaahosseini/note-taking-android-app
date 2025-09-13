package com.example.simple_note_test.data.repos

import com.example.simple_note_test.data.models.*
import com.example.simple_note_test.data.remote.AuthApi
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: AuthApi) {
    suspend fun login(request: LoginRequest) = api.login(request)
    suspend fun register(request: RegisterRequest): RegisterResponse = api.register(request)
    suspend fun changePassword(request: ChangePasswordRequest) = api.changePassword(request)
    suspend fun refreshToken(request: RefreshTokenRequest) = api.refreshToken(request)
    suspend fun getProfile() = api.getProfile()
}
