package com.example.music_player_task.songs.domain.repository

import android.graphics.Bitmap
import arrow.core.Either
import com.example.music_player_task.songs.domain.model.NetworkError
import com.example.music_player_task.songs.domain.model.Songs

interface SongRepository {
    suspend fun getSongs(): Either<NetworkError, Songs>

}

interface SongImageRepository {
    suspend fun getSongImage(imageId: String): Either<NetworkError, Bitmap?>
}