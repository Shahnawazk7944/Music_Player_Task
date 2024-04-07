package com.example.music_player_task.songs.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import arrow.core.Either
import com.example.music_player_task.songs.data.mapper.toImageNetworkError
import com.example.music_player_task.songs.data.mapper.toNetworkError
import com.example.music_player_task.songs.data.remote.SongApi
import com.example.music_player_task.songs.data.remote.SongImageApi
import com.example.music_player_task.songs.domain.model.ImageNetworkError
import com.example.music_player_task.songs.domain.model.NetworkError
import com.example.music_player_task.songs.domain.model.Songs
import com.example.music_player_task.songs.domain.repository.SongImageRepository
import com.example.music_player_task.songs.domain.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.invoke
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val songApi: SongApi
) : SongRepository {

    override suspend fun getSongs(): Either<NetworkError, Songs> {
        return Either.catch {
            songApi.getSongs()
        }.mapLeft { it.toNetworkError() }
    }
}

class SongImageRepositoryImpl @Inject constructor(
    private val songImageApi: SongImageApi
) : SongImageRepository {
    override suspend fun getSongImage(imageId: String): Either<ImageNetworkError, Bitmap?> {
        return try {
            val imageResponse =  songImageApi.getSongImage(imageId = imageId)
            if (imageResponse.isSuccessful) {
                val imageBytes = imageResponse.body()?.bytes()
                return imageBytes?.let {
                    val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                    Either.Right(bitmap)
                } ?: Either.Left("Image fetched but something went wrong".toImageNetworkError())
            } else {
                return Either.Left(
                    imageResponse.code().toString().toImageNetworkError()
                )
            }
        } catch (e: Exception) {
            Either.Left(e.message!!.toImageNetworkError())
        }
    }
}