package com.example.music_player_task.songs.presentation.viewModels

import android.graphics.Bitmap

sealed class MusicPlayerUiEvents {
    data class NavigateTo(val route: String) : MusicPlayerUiEvents()
}