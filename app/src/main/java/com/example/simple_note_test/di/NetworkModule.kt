package com.example.simple_note_test.di

import android.content.Context
import com.example.simple_note_test.data.local.TokenStorage
import com.example.simple_note_test.data.remote.AuthApi
import com.example.simple_note_test.data.remote.AuthInterceptor
import com.example.simple_note_test.data.remote.NotesApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenStorage: TokenStorage): AuthInterceptor = AuthInterceptor { tokenStorage.getAccessToken() }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://simple.darkube.app/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideNotesApi(retrofit: Retrofit): NotesApi = retrofit.create(NotesApi::class.java)

    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage = TokenStorage(context)
}
