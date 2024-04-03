package com.example.music_player_task.songs.di

import com.example.music_player_task.songs.data.remote.SongApi
import com.example.music_player_task.songs.data.remote.SongImageApi
import com.example.music_player_task.songs.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideSongApi(): SongApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SongApi::class.java)

    }

    @Singleton
    @Provides
    fun provideSongImageApi(): SongImageApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
            .create(SongImageApi::class.java)

    }
}