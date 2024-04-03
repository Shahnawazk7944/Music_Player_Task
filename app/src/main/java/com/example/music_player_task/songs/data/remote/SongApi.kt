package com.example.music_player_task.songs.data.remote

import android.graphics.Bitmap
import com.example.music_player_task.songs.domain.model.Songs
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SongApi {
    @GET("items/songs")
    suspend fun getSongs(): Songs
}

interface SongImageApi {
    @GET("assets/{imageId}")
    suspend fun getSongImage(@Path(value = "imageId") imageId: String): Response<ResponseBody>
}