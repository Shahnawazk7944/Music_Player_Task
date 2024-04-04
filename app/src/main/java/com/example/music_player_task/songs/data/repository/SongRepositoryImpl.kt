package com.example.music_player_task.songs.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import arrow.core.Either
import com.example.music_player_task.songs.data.mapper.toNetworkError
import com.example.music_player_task.songs.data.remote.SongApi
import com.example.music_player_task.songs.data.remote.SongImageApi
import com.example.music_player_task.songs.domain.model.NetworkError
import com.example.music_player_task.songs.domain.model.Songs
import com.example.music_player_task.songs.domain.repository.SongImageRepository
import com.example.music_player_task.songs.domain.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    override suspend fun getSongImage(imageId: String): Either<NetworkError, Bitmap?> {
        Log.d("check called getImage","with image id is $imageId")
        //delay(2000)
        return try {
            val image = withContext(Dispatchers.IO) {
                async { songImageApi.getSongImage(imageId = imageId) }.await()
            }
            Log.d("check called api","${image.body()?.bytes()}")

            val imageBytes = image.body()?.bytes()
            val bitmap = imageBytes?.let { BitmapFactory.decodeByteArray(imageBytes, 0, it.size) }
            return Either.Right(bitmap)
        } catch (e: Exception) {
            Either.Left(e.toNetworkError())
        }
    }

}