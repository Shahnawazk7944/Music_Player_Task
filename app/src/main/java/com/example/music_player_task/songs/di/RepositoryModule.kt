package com.example.music_player_task.songs.di

import com.example.music_player_task.songs.data.repository.SongImageRepositoryImpl
import com.example.music_player_task.songs.data.repository.SongRepositoryImpl
import com.example.music_player_task.songs.domain.repository.SongImageRepository
import com.example.music_player_task.songs.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSongRepository(impl: SongRepositoryImpl): SongRepository

    @Binds
    @Singleton
    abstract fun bindSongImageRepository(impl: SongImageRepositoryImpl): SongImageRepository
}