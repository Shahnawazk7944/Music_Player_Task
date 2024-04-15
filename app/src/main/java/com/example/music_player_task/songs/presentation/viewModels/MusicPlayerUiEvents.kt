package com.example.music_player_task.songs.presentation.viewModels

import android.content.Context
import android.graphics.Bitmap
import com.example.music_player_task.songs.domain.model.Song
import java.text.FieldPosition

sealed class MusicPlayerUiEvents {
    data class NavigateTo(val route: String) : MusicPlayerUiEvents()
    data class GetImageBitmap(val url: String) : MusicPlayerUiEvents()
    data class PlaySong(val url: String) : MusicPlayerUiEvents()
    data class PauseSong(val isPause: Boolean ) : MusicPlayerUiEvents()
    data class StopSong(val isStop :Boolean) : MusicPlayerUiEvents()
    data class ReleasePlayer(val releasePlayer:Boolean) : MusicPlayerUiEvents()
    data class SelectTheSong(val selectTheSong: Song) : MusicPlayerUiEvents()
    data class IsSongPlaying(val isSongPlaying:Boolean) : MusicPlayerUiEvents()
    data class PlayingSongIndex(val playingSongIndex:Int) : MusicPlayerUiEvents()
    data class GetColorsFromImage(val imageUrl:String, val context:Context) : MusicPlayerUiEvents()
    data class UpdatePlaybackState(val currentPosition:Int) : MusicPlayerUiEvents()
    data class changeSongIndex(val index:Int) : MusicPlayerUiEvents()
}
