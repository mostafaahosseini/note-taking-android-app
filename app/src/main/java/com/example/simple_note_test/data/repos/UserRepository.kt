package com.example.simple_note_test.data.repos

import com.example.simple_note_test.data.models.UserProfileResponse
import com.example.simple_note_test.data.remote.AuthApi
import javax.inject.Inject

class UserRepository @Inject constructor(private val api: AuthApi) {
    suspend fun getProfile(): UserProfileResponse = api.getProfile()
}
