package com.example.simple_note_test.data.remote

import com.example.simple_note_test.data.models.*
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthApi {
    @POST("api/auth/token/")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register/")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/auth/change-password/")
    suspend fun changePassword(@Body request: ChangePasswordRequest): BaseResponse

    @POST("api/auth/token/refresh/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): AuthResponse

    @GET("api/auth/userinfo/")
    suspend fun getProfile(): UserProfileResponse
}
