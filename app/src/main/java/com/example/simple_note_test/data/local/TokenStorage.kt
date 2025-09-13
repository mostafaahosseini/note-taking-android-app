package com.example.simple_note_test.data.local

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStorage @Inject constructor(@ApplicationContext context: Context) {
    init {
        Log.d("TokenStorage", "TokenStorage created")
    }

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(access: String, refresh: String) {
        prefs.edit().putString("access", access).putString("refresh", refresh).apply()
    }

    fun getAccessToken(): String? = prefs.getString("access", null)
    fun getRefreshToken(): String? = prefs.getString("refresh", null)
    fun clearTokens() { prefs.edit().clear().apply() }
}
