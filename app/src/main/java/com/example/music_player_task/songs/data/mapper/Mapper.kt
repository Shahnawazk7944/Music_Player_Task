package com.example.music_player_task.songs.data.mapper

import com.example.music_player_task.songs.domain.model.ApiError
import com.example.music_player_task.songs.domain.model.ImageApiError
import com.example.music_player_task.songs.domain.model.ImageNetworkError
import com.example.music_player_task.songs.domain.model.NetworkError
import okhttp3.ResponseBody
import java.io.IOException
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Response.error
import retrofit2.Retrofit

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
fun String.toImageNetworkError(): ImageNetworkError {
    val error = when (this) {
        "200" -> ImageApiError.Success
        "404" -> ImageApiError.Failed
        else -> ImageApiError.UnknownError
    }
    return ImageNetworkError(
        error = error,
        t = this
    )
}
