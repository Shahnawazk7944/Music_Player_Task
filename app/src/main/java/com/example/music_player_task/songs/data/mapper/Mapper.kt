package com.example.music_player_task.songs.data.mapper

import com.example.music_player_task.songs.domain.model.ApiError
import com.example.music_player_task.songs.domain.model.NetworkError
import java.io.IOException
import retrofit2.HttpException

fun Throwable.toNetworkError(): NetworkError {
    val error = when (this) {
        is IOException -> ApiError.NetworkError
        is HttpException -> ApiError.UnknownResponse
        else -> ApiError.UnknownError
    }
    return NetworkError(
        error = error,
        t = this
    )
}