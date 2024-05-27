package com.example.cinemahub.data

import com.example.cinemahub.network.CinemaHubApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val cinemaHubRepository: CinemaHubRepository
}

@Module
@InstallIn(SingletonComponent::class)
class DefaultAppContainer : AppContainer {

    private val baseUrl = "http://192.168.0.104:8080/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: CinemaHubApiService by lazy {
        retrofit.create(CinemaHubApiService::class.java)
    }

    @get:Provides
    override val cinemaHubRepository: CinemaHubRepository by lazy {
        NetworkCinemaHubRepository(
            retrofitService
        )
    }
}