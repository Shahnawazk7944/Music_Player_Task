package com.example.music_player_task.songs.presentation.viewModels

import android.graphics.Bitmap
import com.example.music_player_task.songs.domain.model.Song

sealed class MusicPlayerUiEvents {
    data class NavigateTo(val route: String) : MusicPlayerUiEvents()
    data class PlaySong(val url: String) : MusicPlayerUiEvents()
    data class PauseSong(val isPause: Boolean ) : MusicPlayerUiEvents()
    data class StopSong(val isStop :Boolean) : MusicPlayerUiEvents()
    data class ReleasePlayer(val releasePlayer:Boolean) : MusicPlayerUiEvents()
    data class SelectTheSong(val selectTheSong: Song) : MusicPlayerUiEvents()
}